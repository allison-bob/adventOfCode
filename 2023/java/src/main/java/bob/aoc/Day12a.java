package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day12a extends BaseClass<List<Day12Row>> {

    public static void main(String[] args) {
        new Day12a().run(args, "");
    }

    public Day12a() {
        super(false);
        setParser(new ObjectParser<>(Day12Row::new));
    }

    @Override
    public void solve(List<Day12Row> entries) {
        LOG.info("Read {} entries", entries.size());
        
        long sum = 0;
        for (Day12Row row : entries) {
            long ct = checkRow(row);
            sum += ct;
            LOG.debug("Row {}: {}", row, ct);
        }
        LOG.info("Total arrangements: {}", sum);
    }
    
    private long checkRow(Day12Row row) {
        int pos = row.springs.indexOf(Day12Spring.UNKNOWN);
        if (pos < 0) {
            if (row.countCheck()) {
                return 1;
            } else {
                return 0;
            }
        }
        return checkRow(new Day12Row(row.springs, pos, Day12Spring.WORKS, row.counts))
                + checkRow(new Day12Row(row.springs, pos, Day12Spring.BROKEN, row.counts));
    }
}
