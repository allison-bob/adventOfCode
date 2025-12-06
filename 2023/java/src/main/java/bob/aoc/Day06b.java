package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day06b extends BaseClass<Map<String, Day06b.Stats>> {

    public static class Stats {

        public String id;
        public List<Long> races = new ArrayList<>();

        public Stats(String line) {
            String[] bits = line.replace(" ", "").split(":");
            id = bits[0];
            for (int i = 1; i < bits.length; i++) {
                races.add(Long.valueOf(bits[i]));
            }
        }
    }

    public static void main(String[] args) {
        new Day06b().run(args, "");
    }

    public Day06b() {
        super(false);
        setParser(new LineObjectMapParser<>(line -> new Stats(line), s -> s.id));
    }

    @Override
    public void solve(Map<String, Stats> data) {
        List<Long> times = data.get("Time").races;
        List<Long> dists = data.get("Distance").races;
        LOG.info("Read {} races", times.size());

        long answer = 1;
        for (int j = 0; j < times.size(); j++) {
            int count = 0;
            for (int i = 0; i < times.get(j); i++) {
                if ((i * (times.get(j) - i)) > dists.get(j)) {
                    count++;
                }
            }
            LOG.info("For race {}, count is {}", j, count);
            answer *= count;
        }
        
        LOG.info("answer is {}", answer);
    }
}
