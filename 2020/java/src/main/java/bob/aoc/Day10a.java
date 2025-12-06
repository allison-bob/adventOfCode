package bob.aoc;

import bob.util.Assert;
import bob.util.BaseClass;
import bob.parser.SingleIntegerParser;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day10a extends BaseClass<List<Integer>> {

    public static void main(String[] args) {
        new Day10a().run(args, "a");
    }

    public Day10a() {
        super(false);
        setParser(new SingleIntegerParser());
    }

    @Override
    public void solve(List<Integer> adapters) {
        LOG.info("read {} adapters", adapters.size());

        // Sort the list
        Collections.sort(adapters);

        // Add entries for the two ends
        adapters.add(0, 0);
        adapters.add(adapters.get(adapters.size() - 1) + 3);
        
        // Find the gap sizes between adapters
        int[] counts = new int[]{0, 0, 0, 0, 0};
        for (int i = 1; i < adapters.size(); i++) {
            Assert.that(((adapters.get(i) - adapters.get(i - 1)) < 4), "gap between adapters greater than 3");
            counts[adapters.get(i) - adapters.get(i - 1)]++;
        }
        LOG.info("counts: {}", Arrays.toString(counts));
        LOG.info("answer = {}", (counts[3] * counts[1]));
    }
}
