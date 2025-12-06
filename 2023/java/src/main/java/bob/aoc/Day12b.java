package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day12b extends BaseClass<List<Day12Row>> {

    Map<String, Long> cache = new HashMap<>();

    public static void main(String[] args) {
        new Day12b().run(args, "");
    }

    public Day12b() {
        super(false);
        setParser(new ObjectParser<>(Day12Row::new));
    }

    @Override
    public void solve(List<Day12Row> entries) {
        LOG.info("Read {} entries", entries.size());

        long sum = 0;
        for (Day12Row row : entries) {
            row.unfold();
            long ct = checkRow(row);
            sum += ct;
            LOG.info("Row {}: {}", row, ct);
        }
        LOG.info("Total arrangements: {}", sum);
    }

    private long checkRow(Day12Row row) {
        LOG.debug("Checking {}", row);

        // Have we already seen this result?
        if (cache.containsKey(row.toString())) {
            LOG.debug("returned cache result");
            return cache.get(row.toString());
        }

        // Are we out of unknowns?
        if (row.springs.indexOf(Day12Spring.UNKNOWN) < 0) {
            LOG.debug("... No unknowns: currCounts()={}, counts={}", row.currCounts(), row.counts);
            if (row.countCheck()) {
                LOG.debug("<-- 1");
                return 1;
            } else {
                LOG.debug("<-- 0");
                return 0;
            }
        }

        // Are we out of groups?
        if (row.counts.isEmpty()) {
            if (row.springs.indexOf(Day12Spring.BROKEN) < 0) {
                // All remaining springs are either working or unknown (assumed working)
                LOG.debug("<-- 1 (No groups and no broken)");
                return 1;
            } else {
                // No groups but at least one broken spring
                LOG.debug("<-- 0 (No groups and a broken)");
                return 0;
            }
        }

        // Act based on the first spring in the list
        switch (row.springs.get(0)) {
            case WORKS:
                // We can remove the spring from the front, no change to the remaining groups
                long v1 = checkRow(new Day12Row(row.springs.subList(1, row.springs.size()), row.counts));
                cache.put(row.toString(), v1);
                return v1;
            case UNKNOWN:
                // Try each possibility
                long v2 = checkRow(new Day12Row(row.springs, 0, Day12Spring.WORKS, row.counts))
                        + checkRow(new Day12Row(row.springs, 0, Day12Spring.BROKEN, row.counts));
                cache.put(row.toString(), v2);
                return v2;
            case BROKEN:
                // To see if we can fill the first group
                int groupCt = row.counts.get(0);
                for (int i = 0; i < groupCt; i++) {
                    switch (row.springs.get(i)) {
                        case WORKS:
                            // Not enough brokens to meet required count
                            LOG.debug("<-- 0 (group too small)");
                            return 0;
                        case UNKNOWN:
                            // This must be a broken
                            long v3 = checkRow(new Day12Row(row.springs, i, Day12Spring.BROKEN, row.counts));
                            cache.put(row.toString(), v3);
                            return v3;
                        case BROKEN:
                            // Still good so far, advance to the next spring
                            break;
                    }
                }
                // Group has enough broken, act based on next spring
                switch (row.springs.get(groupCt)) {
                    case UNKNOWN:
                        // This must be a working
                        long v4 = checkRow(new Day12Row(row.springs, groupCt, Day12Spring.WORKS, row.counts));
                        cache.put(row.toString(), v4);
                        return v4;
                    case BROKEN:
                        // Too many brokens to meet required count
                        LOG.debug("<-- 0 (group too large)");
                        return 0;
                    case WORKS:
                        // Drop this group and continue
                        long v5 = checkRow(new Day12Row(row.springs.subList(groupCt, row.springs.size()),
                                row.counts.subList(1, row.counts.size())));
                        cache.put(row.toString(), v5);
                        return v5;
                }
        }

        throw Assert.failed(null, "How did we get here?");
    }
}
