package bob.aoc;

import bob.util.Assert;
import bob.util.BaseClass;
import bob.parser.SingleIntegerParser;
import java.util.Collections;
import java.util.List;

public class Day10b extends BaseClass<List<Integer>> {

    public static void main(String[] args) {
        new Day10b().run(args, "a");
    }

    public Day10b() {
        super(false);
        setParser(new SingleIntegerParser());
    }

    // Number of options between consecutive 3-jolt jumps based on the number
    // of 1-jolt jumps between them
    private final int[] OPTION_CT = new int[]{1, 1, 2, 4, 7};

    @Override
    public void solve(List<Integer> adapters) {
        LOG.info("read {} adapters", adapters.size());

        // Sort the list
        Collections.sort(adapters);

        // Add entries for the two ends
        adapters.add(0, 0);
        adapters.add(adapters.get(adapters.size() - 1) + 3);

        // Find the total number of options as the product of the number of
        // options between successive 3-jolt jumps
        int lastThree = 0;
        long optct = 1;
        for (int i = 1; i < adapters.size(); i++) {
            if ((adapters.get(i) - adapters.get(i - 1)) == 3) {
                int oneGapCt = adapters.get(i - 1) - adapters.get(lastThree);
                Assert.that((oneGapCt < OPTION_CT.length), "run of 1 gaps too large");
                optct *= OPTION_CT[oneGapCt];
                lastThree = i;
            }
        }
        LOG.info("Option count: {}", optct);
    }
}
