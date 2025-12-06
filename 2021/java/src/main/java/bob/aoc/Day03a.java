package bob.aoc;

import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day03a extends BaseClass<Grid2D<Day03Bit>> {

    public static void main(String[] args) {
        new Day03a().run(args, "");
    }

    public Day03a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day03Bit::byChar));
    }

    @Override
    public void solve(Grid2D<Day03Bit> report) {
        LOG.info("report contains {} entries", report.getSize().getY());
        int xLen = report.getSize().getX();
        int yLen = report.getSize().getY();
        int[] sums = new int[xLen];
        
        for (int y = 0; y < yLen; y++) {
            for (int x = 0; x < xLen; x++) {
                sums[x] += report.get(x, y).getValue();
            }
        }
        
        long epsilon = 0;
        long gamma = 0;
        for (int i = 0; i < sums.length; i++) {
            epsilon <<= 1;
            gamma <<= 1;
            if ((2 * sums[i]) > yLen) {
                // 1 more common
                gamma++;
            } else {
                // 0 more common
                epsilon++;
            }
        }
        
        LOG.info("gamma = {}, epsilon = {}", gamma, epsilon);
        LOG.info("Answer is {}", (epsilon * gamma));
    }
}
