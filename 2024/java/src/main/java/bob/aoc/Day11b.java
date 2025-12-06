package bob.aoc;

import bob.parser.LineListParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11b extends BaseClass<List<List<Long>>> {

    public static void main(String[] args) {
        new Day11b().run(args, "");
    }

    public Day11b() {
        super(false);
        setParser(new LineListParser<>(" ", Long::valueOf));
    }

    @Override
    public void solve(List<List<Long>> data) {
        Assert.that((data.size() == 1), "More than one line read");
        List<Long> stones = data.get(0);
        LOG.info("Read {} stones", stones.size());

        // Map of stone inscription to blink result
        Map<Long, List<Long>> transforms = new HashMap<>();
        // Map of stone inscription to count
        Map<Long, Long> counts = new HashMap<>();
        addStones(counts, stones, 1L);

        LOG.debug("Stones: {}", counts);
        for (int i = 0; i < 75; i++) {
            Map<Long, Long> newCounts = new HashMap<>();

            for (Long s : counts.keySet()) {
                if (transforms.containsKey(s)) {
                    addStones(newCounts, transforms.get(s), counts.get(s));
                } else {
                    List<Long> newStones = new ArrayList<>();
                    String ss = s.toString();
                    if (s == 0) {
                        newStones.add(1L);
                    } else if ((ss.length() % 2) == 0) {
                        int half = ss.length() / 2;
                        newStones.add(Long.valueOf(ss.substring(0, half)));
                        newStones.add(Long.valueOf(ss.substring(half)));
                    } else {
                        newStones.add(2024 * s);
                    }
                    transforms.put(s, newStones);
                    addStones(newCounts, newStones, counts.get(s));
                }
            }

            LOG.debug("Stones: {}", newCounts);
            counts = newCounts;
        }

        long stoneCt = counts.values().stream()
                .mapToLong(l -> l)
                .sum();
        LOG.info("There are {} stones", stoneCt);
    }

    private void addStones(Map<Long, Long> counts, List<Long> toAdd, Long replicaCt) {
        for (Long a : toAdd) {
            if (counts.containsKey(a)) {
                counts.put(a, counts.get(a) + replicaCt);
            } else {
                counts.put(a, replicaCt);
            }
        }
    }
}
