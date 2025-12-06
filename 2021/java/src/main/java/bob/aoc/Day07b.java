package bob.aoc;

import bob.parser.IntegerListParser;
import bob.util.BaseClass;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

public class Day07b extends BaseClass<List<Integer>> {

    public static void main(String[] args) {
        new Day07b().run(args, "");
    }

    public Day07b() {
        super(false);
        setParser(new IntegerListParser());
    }

    @Override
    public void solve(List<Integer> starts) {
        LOG.info("read {} starting positions", starts.size());

        // Find the min and max positions
        long min = starts.stream()
                .mapToLong(l -> l)
                .min()
                .getAsLong();
        long max = starts.stream()
                .mapToLong(l -> l)
                .max()
                .getAsLong();

        // Try each position to find min fuel requirement
        long minfuel = Long.MAX_VALUE;
        Map<Long, Long> costs = new HashMap<>();
        for (long i = min; i <= max; i++) {
            long thisfuel = 0;
            for (int j = 0; j < starts.size(); j++) {
                long dist = Math.abs(starts.get(j) - i);
                if (costs.containsKey(dist)) {
                    thisfuel += costs.get(dist);
                } else {
                    long cost = LongStream.rangeClosed(1, dist).sum();
                    costs.put(dist, cost);
                    thisfuel += cost;
                }
            }
            if (thisfuel < minfuel) {
                minfuel = thisfuel;
            }
            LOG.debug("At {} fuel is {}, min is {}", i, thisfuel, minfuel);
        }

        LOG.info("Answer is {}", minfuel);
    }
}
