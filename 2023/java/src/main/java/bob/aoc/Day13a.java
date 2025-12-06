package bob.aoc;

import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day13a extends BaseClass<List<Grid2D<Day13Pattern>>> {

    public static void main(String[] args) {
        new Day13a().run(args, "");
    }

    public Day13a() {
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
                for (int y = 0; ((y < grid.getSize().getY()) && (col >= 0)); y++) {
                    if (grid.get(x, y) != grid.get((x + 1), y)) {
                        col = -1;
                    }
                }
                if (!checkMirrorCol(grid, col)) {
                    col = -1;
                }
            }

            // Look for a repeated row
            int row = -1;
            for (int y = 0; ((y < (grid.getSize().getY() - 1)) && (row < 0)); y++) {
                row = y;
                for (int x = 0; ((x < grid.getSize().getX()) && (row >= 0)); x++) {
                    if (grid.get(x, y) != grid.get(x, (y + 1))) {
                        row = -1;
                    }
                }
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
        for (int dx = 0; dx < (grid.getSize().getX() - 1); dx++) {
            if ((grid.get((col - dx), 0) == null) || (grid.get((col + dx + 1), 0) == null)) {
                break;
            }
            for (int y = 0; y < grid.getSize().getY(); y++) {
                if (grid.get((col - dx), y) != grid.get((col + dx + 1), y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkMirrorRow(Grid2D<Day13Pattern> grid, int row) {
        for (int dy = 0; dy < (grid.getSize().getY() - 1); dy++) {
            if ((grid.get(0, (row - dy)) == null) || (grid.get(0, (row + dy + 1)) == null)) {
                break;
            }
            for (int x = 0; x < grid.getSize().getX(); x++) {
                if (grid.get(x, (row - dy)) != grid.get(x, (row + dy + 1))) {
                    return false;
                }
            }
        }
        return true;
    }
}
