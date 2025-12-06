package bob.aoc;

import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day13b extends BaseClass<List<Grid2D<Day13Pattern>>> {

    public static void main(String[] args) {
        new Day13b().run(args, "");
    }

    public Day13b() {
        super(false);
        setParser(new ObjectListParser<>(new Grid2DParser<>(false, false, Day13Pattern::byChar)));
    }

    @Override
    public void solve(List<Grid2D<Day13Pattern>> entries) {
        LOG.info("Read {} entries", entries.size());

        long sum = 0;

        for (Grid2D<Day13Pattern> grid : entries) {
            LOG.debug("Processing grid:\n{}", grid.dump(Day13Pattern::getSymbol));

            // Look for a repeated column
            int col = -1;
            for (int x = 0; ((x < (grid.getSize().getX() - 1)) && (col < 0)); x++) {
                col = x;
                if (!checkMirrorCol(grid, col)) {
                    col = -1;
                }
            }

            // Look for a repeated row
            int row = -1;
            for (int y = 0; ((y < (grid.getSize().getY() - 1)) && (row < 0)); y++) {
                row = y;
                if (!checkMirrorRow(grid, row)) {
                    row = -1;
                }
            }
            long score = ((col + 1) * 1) + ((row + 1) * 100);
            LOG.debug("Found repeat at c={}, r={}, v={}", col, row, score);
            sum += score;
        }

        LOG.info("Total is {}", sum);
    }

    private boolean checkMirrorCol(Grid2D<Day13Pattern> grid, int col) {
        LOG.debug("mirror check on col {}", col);
        boolean result = false;
        for (int dx = 0; dx < (grid.getSize().getX() - 1); dx++) {
            if ((grid.get((col - dx), 0) == null) || (grid.get((col + dx + 1), 0) == null)) {
                break;
            }
            for (int y = 0; y < grid.getSize().getY(); y++) {
                if (grid.get((col - dx), y) != grid.get((col + dx + 1), y)) {
                    if (result) {
                        // Already found 1 diff, so this fails
                        return false;
                    }
                    result = true;
                }
            }
        }
        return result;
    }

    private boolean checkMirrorRow(Grid2D<Day13Pattern> grid, int row) {
        LOG.debug("mirror check on row {}", row);
        boolean result = false;
        for (int dy = 0; dy < (grid.getSize().getY() - 1); dy++) {
            if ((grid.get(0, (row - dy)) == null) || (grid.get(0, (row + dy + 1)) == null)) {
                break;
            }
            for (int x = 0; x < grid.getSize().getX(); x++) {
                if (grid.get(x, (row - dy)) != grid.get(x, (row + dy + 1))) {
                    if (result) {
                        // Already found 1 diff, so this fails
                        return false;
                    }
                    result = true;
                }
            }
        }
        return result;
    }
}
