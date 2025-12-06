package bob.aoc;

import bob.util.Assert;
import bob.util.BaseClass;
import bob.parser.SingleLongParser;
import java.util.ArrayList;
import java.util.List;

public class Day09b extends BaseClass<List<Long>> {

    public static void main(String[] args) {
        new Day09b().run(args, "");
    }

    public Day09b() {
        super(false);
        setParser(new SingleLongParser());
    }

    @Override
    public void solve(List<Long> data) {
        LOG.info("read {} numbers", data.size());
        int preambleLength = (isRealData() ? 25 : 5);
        LOG.info("Preamble length is {}", preambleLength);
        Assert.that((data.size() > preambleLength), "data shorter than preamble");
        
        // Find the first bad number
        long badval = -1;
        for (int i = preambleLength; ((i < data.size()) && (badval == -1)); i++) {
            if (!validate(data.subList((i - preambleLength), i), data.get(i))) {
                LOG.info("First failure is at {}: {}", i, data.get(i));
                badval = data.get(i);
            }
        }
        
        // Find the proper set of numbers
        List<Long> found = findSublist(data, badval);
        LOG.debug("found has {} items", found.size());
        long min = found.stream().mapToLong(l -> l).min().getAsLong();
        long max = found.stream().mapToLong(l -> l).max().getAsLong();
        LOG.info("min={}, max={}, sum={}", min, max, (min + max));
    }

    private boolean validate(List<Long> items, Long value) {
        LOG.debug("Looking for sum of {} in {}", value, items);
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = i + 1; j < items.size(); j++) {
                if ((items.get(i) + items.get(j)) == value) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Long> findSublist(List<Long> data, Long invalid) {
        for (int i = 0; i < data.size() - 1; i++) {
            long sum = data.get(i);
            for (int j = i + 1; (j < data.size()) && (sum < invalid); j++) {
                sum += data.get(j);
                if (sum == invalid) {
                    LOG.debug("i={},j={}", i, j);
                    return data.subList(i, j + 1);
                }
            }
        }
        return new ArrayList<>();
    }
}
