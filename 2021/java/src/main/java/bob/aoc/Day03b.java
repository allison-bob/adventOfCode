package bob.aoc;

import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day03b extends BaseClass<Grid2D<Day03Bit>> {

    public static void main(String[] args) {
        new Day03b().run(args, "");
    }

    public Day03b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day03Bit::byChar));
    }

    @Override
    public void solve(Grid2D<Day03Bit> report) {
        LOG.info("report contains {} entries", report.getSize().getY());
        int xLen = report.getSize().getX();
        int yLen = report.getSize().getY();
        
        Grid2D<Day03Bit> remaining = report;
        for (int i = 0; ((i < xLen) && (remaining.getSize().getY() > 1)); i++) {
            remaining = filter(remaining, i, true, Day03Bit.ON);
            LOG.debug("bit {} filtered to {}", i, remaining.dump(Day03Bit::getSymbol));
        }
        long oxy = convert(remaining);
        
        remaining = report;
        for (int i = 0; ((i < xLen) && (remaining.getSize().getY() > 1)); i++) {
            remaining = filter(remaining, i, false, Day03Bit.OFF);
            LOG.debug("bit {} filtered to {}", i, remaining.dump(Day03Bit::getSymbol));
        }
        long co2 = convert(remaining);

        LOG.info("oxy = {}, co2 = {}", oxy, co2);
        LOG.info("Answer is {}", (oxy * co2));
    }
    
    private long convert(Grid2D<Day03Bit> in) {
        long retval = 0;
        List<Day03Bit> row = in.rowStream().findFirst().orElseThrow();
        for (Day03Bit b : row) {
            retval <<= 1;
            retval += b.getValue();
        }
        return retval;
    }

    private Grid2D<Day03Bit> filter(Grid2D<Day03Bit> in, int pos, boolean useMostCommon, Day03Bit ifTied) {
        // Divide into two sets of rows based on value in specified position
        Map<Day03Bit, List<List<Day03Bit>>> grouped = in.rowStream()
                .collect(Collectors.groupingBy(r -> r.get(pos)));
        
        // Pick the right set of rows
        List<List<Day03Bit>> rows;
        if (grouped.get(Day03Bit.ON).size() == grouped.get(Day03Bit.OFF).size()) {
            // Both lists are the same size, select the tie breaker
            rows = grouped.get(ifTied);
        } else {
            if ((grouped.get(Day03Bit.ON).size() > grouped.get(Day03Bit.OFF).size()) == useMostCommon) {
                // ON list is longer and useMostCommon is true
                // ON list is shorter and useMostCommon is false
                rows = grouped.get(Day03Bit.ON);
            } else {
                // OFF list is longer and useMostCommon is true
                // OFF list is shorter and useMostCommon is false
                rows = grouped.get(Day03Bit.OFF);
            }
        }
        
        // Build the result
        Grid2D<Day03Bit> result = new Grid2D<>();
        rows.forEach(result::addRow);
        return result;
    }
}
