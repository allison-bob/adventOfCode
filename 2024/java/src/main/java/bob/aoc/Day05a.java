package bob.aoc;

import bob.parser.LineListParser;
import bob.parser.TwoFormatParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.List;

public class Day05a extends BaseClass<TwoFormatParser.Output<List<List<Long>>, List<List<Long>>>> {

    List<List<Long>> rules;
    List<List<Long>> updates;

    public static void main(String[] args) {
        new Day05a().run(args, "");
    }

    public Day05a() {
        super(false);
        setParser(new TwoFormatParser<>(
                new LineListParser<>("\\|", Long::valueOf),
                new LineListParser<>(",", Long::valueOf)
        ));
    }

    @Override
    public void solve(TwoFormatParser.Output<List<List<Long>>, List<List<Long>>> data) {
        rules = data.first;
        updates = data.rest;
        LOG.info("Read {} rules, {} updates", rules.size(), updates.size());

        long total = 0;
        for (List<Long> update : updates) {
            total += checkPageOrder(update);
            LOG.debug("Total is now {}", total);
        }

        LOG.info("Total is {}", total);
    }

    private long checkPageOrder(List<Long> update) {
        LOG.debug("Checking {}", update);
        
        // Check the rules
        for (List<Long> rule : rules) {
            int leftPos = update.indexOf(rule.get(0));
            int rightPos = update.indexOf(rule.get(1));
            if ((leftPos > -1) && (rightPos > -1)) {
                if (leftPos > rightPos) {
                    LOG.debug("... error: {} prints after {}", rule.get(0), rule.get(1));
                    return 0;
                }
            }
        }

        LOG.debug("... is correct");
        Assert.that(((update.size() % 2) == 1), "Update page set has even number of pages");
        return update.get(update.size() / 2);
    }
}
