package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day04b extends BaseClass<Grid2D<Integer>> {

    private Grid2D<Integer> board;

    public static void main(String[] args) {
        new Day04b().run(args, "");
    }

    public Day04b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, this::translate));
    }

    private int translate(char c) {
        return switch (c) {
            case 'A' ->
                1;
            case 'M' ->
                2;
            case 'S' ->
                3;
            default ->
                0;
        };
    }

    @Override
    public void solve(Grid2D<Integer> board) {
        this.board = board;
        Coord2D size = board.getSize();
        LOG.info("Read {} word search", size);

        // Scan each grid cell for an A
        int foundCt = 0;
        for (int y = 1; y < (size.getY() - 1); y++) {
            for (int x = 1; x < (size.getX() - 1); x++) {
                foundCt += checkForA(x, y);
            }
        }

        LOG.info("Found {} instances", foundCt);
    }

    // Check to see if this grid element is an A
    private int checkForA(int x, int y) {
        if (board.get(x, y) == 1) {
            if ((board.get(x + 1, y + 1) + board.get(x - 1, y - 1)) == 5) {
                if ((board.get(x + 1, y - 1) + board.get(x - 1, y + 1)) == 5) {
                    return 1;
                }
            }
        }

        return 0;
    }
}
