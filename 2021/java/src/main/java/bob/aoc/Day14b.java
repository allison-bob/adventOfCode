package bob.aoc;

import bob.parser.ObjectListParser;
import bob.parser.ObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class Day14b extends BaseClass<TwoFormatParser.Output<List<String>, List<Map<String, String>>>> {

    public static void main(String[] args) {
        new Day14b().run(args, "");
    }

    public Day14b() {
        super(false);
        setParser(new TwoFormatParser<>(
                new ObjectParser<>(line -> line),
                new ObjectListParser<>(HashMap::new, this::parseRule)
        ));
    }

    private void parseRule(Map<String, String> map, String line) {
        String[] bits = line.split(" -> ");
        map.put(bits[0], bits[1]);
    }

    @Override
    public void solve(TwoFormatParser.Output<List<String>, List<Map<String, String>>> data) {
        String poly = data.first.get(0);
        Map<String, String> rules = data.rest.get(0);
        LOG.info("read {} rules", rules.size());
        
        // Create the computation objects
        Map<String, Long> pairs = new HashMap<>();
        for (int j = 0; j < (poly.length() - 1); j++) {
            addToMap(pairs, poly.charAt(j), poly.charAt(j + 1), 1);
        }
        long[] quans = new long[256];
        poly.chars().forEach(c -> quans[c]++);

        // Do each insertion step
        for (int i = 0; i < 40; i++) {
            Map<String, Long> newPairs = new HashMap<>();
            pairs.forEach((pair, currCount) -> {
                Assert.that((currCount != null), "null count");
                
                // Get the element to insert
                Assert.that(rules.containsKey(pair), "missing rule");
                char toInsert = rules.get(pair).charAt(0);
                
                // Add the pairs created by the insertion
                addToMap(newPairs, pair.charAt(0), toInsert, currCount);
                addToMap(newPairs, toInsert, pair.charAt(1), currCount);
                
                // Add instances of the inserted element
                quans[toInsert] += currCount;
            });
            
            // Save the results for the next loop
            pairs = newPairs;
            LOG.debug("polymer length is " + LongStream.of(quans).sum());
            LOG.debug("Polymer contains " + pairs.size() + " unique pairs");
        }

        // Find the answer
        long max = LongStream.of(quans)
                .filter(q -> q > 0)
                .max().getAsLong();
        long min = LongStream.of(quans)
                .filter(q -> q > 0)
                .min().getAsLong();

        LOG.info("Answer is {}", (max - min));
    }

    // Adds the element pair to the map in the specified quantity; if the map already
    // contains the pair, the current quantity is added to the existing value
    private void addToMap(Map<String, Long> map, char c1, char c2, long ct) {
        map.compute(new String(new char[] {c1, c2}), (k, v) -> (v == null) ? ct : v + ct);
    }
}
