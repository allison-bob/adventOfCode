package bob.aoc;

import bob.util.Assert;
import bob.util.BaseClass;
import bob.parser.SingleLongParser;
import java.util.List;

public class Day09a extends BaseClass<List<Long>> {

    public static void main(String[] args) {
        new Day09a().run(args, "");
    }

    public Day09a() {
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
        boolean done = false;
        for (int i = preambleLength; ((i < data.size()) && (!done)); i++) {
            if (!validate(data.subList((i - preambleLength), i), data.get(i))) {
                LOG.info("First failure is at {}: {}", i, data.get(i));
                done = true;
            }
        }
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
}
