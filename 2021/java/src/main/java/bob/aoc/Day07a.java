package bob.aoc;

import bob.parser.IntegerListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day07a extends BaseClass<List<Integer>> {

    public static void main(String[] args) {
        new Day07a().run(args, "");
    }

    public Day07a() {
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
        for (long i = min; i <= max; i++) {
            long pos = i;
            long thisfuel = starts.stream()
                    .mapToLong(s -> s)
                    .map(s -> Math.abs(s - pos))
                    .sum();
            if (thisfuel < minfuel) {
                minfuel = thisfuel;
            }
            LOG.debug("At {} fuel is {}, min is {}", i, thisfuel, minfuel);
        }

        LOG.info("Answer is {}", minfuel);
    }
}
