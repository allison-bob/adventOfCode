package bob.aoc;

import bob.parser.ObjectListParser;
import bob.parser.ObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.BaseClass;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Day14a extends BaseClass<TwoFormatParser.Output<List<String>, List<Map<String, String>>>> {

    public static void main(String[] args) {
        new Day14a().run(args, "");
    }

    public Day14a() {
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
        StringBuilder poly = new StringBuilder(data.first.get(0));
        Map<String, String> rules = data.rest.get(0);
        LOG.info("read {} rules", rules.size());

        // Do each insertion step
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < (poly.length() - 1); j++) {
                String pair = poly.substring(j, (j + 2));
                if (rules.containsKey(pair)) {
                    j++;
                    poly.insert(j, rules.get(pair));
                }
            }

            LOG.debug("After {} steps, polymer is {}", (i + 1), poly);
        }
        
        // Find the element quantities
        int[] quans = new int[256];
        poly.chars().forEach(c -> quans[c]++);
        LOG.debug("Freqs: {}", quans);
        
        // Find the answer
        int max = IntStream.of(quans)
                .filter(q -> q > 0)
                .max().getAsInt();
        int min = IntStream.of(quans)
                .filter(q -> q > 0)
                .min().getAsInt();

        LOG.info("Answer is {}", (max - min));
    }
}
