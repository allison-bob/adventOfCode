package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day04a extends BaseClass<Grid2D<Integer>> {

    private Grid2D<Integer> board;

    public static void main(String[] args) {
        new Day04a().run(args, "");
    }

    public Day04a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, this::translate));
    }

    private int translate(char c) {
        return switch (c) {
            case 'X' ->
                1;
            case 'M' ->
                2;
            case 'A' ->
                3;
            case 'S' ->
                4;
            default ->
                0;
        };
    }

    @Override
    public void solve(Grid2D<Integer> board) {
        this.board = board;
        Coord2D size = board.getSize();
        LOG.info("Read {} word search", size);

        // Scan each grid cell for an X
        int foundCt = 0;
        for (int y = 0; y < size.getY(); y++) {
            for (int x = 0; x < size.getX(); x++) {
                foundCt += checkForX(x, y);
            }
        }
        
        LOG.info("Found {} instances", foundCt);
    }

    // Check to see if this grid element is an X
    private int checkForX(int x, int y) {
        int result = 0;

        if ((board.get(x, y) != null) && (board.get(x, y) == 1)) {
            result = board.neighborStream(new Coord2D(x, y), this::checkForM)
                    .mapToInt(Integer::intValue)
                    .sum();
        }

        return result;
    }

    // Check to see if this grid element is an M
    private int checkForM(Coord2D start, Coord2D offset) {
        int dx = start.getX() + offset.getX();
        int dy = start.getY() + offset.getY();

        if ((board.get(dx, dy) != null) && (board.get(dx, dy) == 2)) {
            return checkForA(dx, dy, offset);
        }

        return 0;
    }

    // Check to see if this grid element is an A
    private int checkForA(int x, int y, Coord2D offset) {
        int dx = x + offset.getX();
        int dy = y + offset.getY();

        if ((board.get(dx, dy) != null) && (board.get(dx, dy) == 3)) {
            return checkForS(dx, dy, offset);
        }

        return 0;
    }

    // Check to see if this grid element is an S
    private int checkForS(int x, int y, Coord2D offset) {
        int dx = x + offset.getX();
        int dy = y + offset.getY();

        if ((board.get(dx, dy) != null) && (board.get(dx, dy) == 4)) {
            return 1;
        }

        return 0;
    }
}
