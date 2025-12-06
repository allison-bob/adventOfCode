package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day09b extends BaseClass<List<Day09History>> {

    public static void main(String[] args) {
        new Day09b().run(args, "");
    }

    public Day09b() {
        super(false);
        setParser(new ObjectParser<>(Day09History::new));
    }

    @Override
    public void solve(List<Day09History> entries) {
        LOG.info("Read {} entries", entries.size());
        
        for (Day09History hist : entries) {
            LOG.debug("Starting with {}", hist);
            while (!hist.sequenceComplete()) {
                hist.findDiffs();
                LOG.debug("Next step {}", hist.toStringLast());
            }
        }
        
        long sum = 0;
        for (Day09History hist : entries) {
            LOG.debug("Starting with {}", hist);
            sum += hist.addFirst();
            LOG.debug("Finished with {}", hist);
        }
        
        LOG.info("total: {}", sum);
    }
}
