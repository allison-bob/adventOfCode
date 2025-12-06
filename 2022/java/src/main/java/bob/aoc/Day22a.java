package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.parser.ObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day22a extends BaseClass<TwoFormatParser.Output<List<String>, List<List<Day22Instruction>>>> {

    private static final Pattern INSTR_PATTERN = Pattern.compile("[LR]");
    private static final Coord2D[] DIRECTIONS = new Coord2D[]{
        new Coord2D(1, 0), new Coord2D(0, 1), new Coord2D(-1, 0), new Coord2D(0, -1)
    };
    private int dirPtr = 0;
    private boolean hitWall;

    public static void main(String[] args) {
        new Day22a().run(args, "");
    }

    public Day22a() {
        super(false);
        setParser(new TwoFormatParser<>(
                new ObjectParser<>(line -> line),
                new ObjectParser<>(this::parseLine)
        ));
    }

    private List<Day22Instruction> parseLine(String line) {
        List<Day22Instruction> result = new ArrayList<>();
        Matcher m = INSTR_PATTERN.matcher(line);
        int lastDir = 0;
        while (m.find()) {
            //LOG.debug("matcher {}/{} {} {}", m.start(), m.end(), line.substring(m.start(), m.end()),
            //        line.substring(lastDir, m.start()));
            result.add(new Day22Instruction(line.substring(m.start(), m.end()),
                    Integer.parseInt(line.substring(lastDir, m.start()))));
            lastDir = m.end();
        }
        //LOG.debug("remainder {}", line.substring(lastDir));
        result.add(new Day22Instruction(" ", Integer.parseInt(line.substring(lastDir))));
        return result;
    }

    @Override
    public void solve(TwoFormatParser.Output<List<String>, List<List<Day22Instruction>>> data) {
        List<Day22Instruction> instrs = data.rest.get(0);
        Grid2D<Day22Tile> map = makeMap(data.first);
        LOG.info("read {} instructions and {} grid", instrs.size(), map.getSize());

        Coord2D position = null;
        for (int x = 0; x < map.getSize().getX(); x++) {
            if (map.get(x, 1) == Day22Tile.OPEN) {
                position = new Coord2D(x, 1);
                break;
            }
        }
        for (Day22Instruction instr : instrs) {
            LOG.debug("Processing instruction {} at {}/{}", instr, position, DIRECTIONS[dirPtr]);
            // Do the move
            hitWall = false;
            for (int i = 0; ((i < instr.getMove()) && (!hitWall)); i++) {
                position = findNeighbor(map, position, position, DIRECTIONS[dirPtr]);
            }
            LOG.debug("   Position now {}", position);

            // Do the turn
            dirPtr = (dirPtr + instr.getTurn()) % DIRECTIONS.length;
            LOG.debug("   Direction now {}", DIRECTIONS[dirPtr]);
        }

        LOG.info("Answer is {}*1000 + {}*4 + {} = {}", position.getY(), position.getX(), dirPtr,
                ((1000 * position.getY()) + (4 * position.getX()) + dirPtr));
    }

    private Grid2D<Day22Tile> makeMap(List<String> in) {
        // Find the length of the longest line
        int maxlen = in.stream()
                .mapToInt(String::length)
                .max().getAsInt();

        // Pad all lines as approrpiate
        for (int i = 0; i < in.size(); i++) {
            String s = in.get(i);
            if (s.length() < maxlen) {
                StringBuilder sb = new StringBuilder(s);
                while (sb.length() < maxlen) {
                    sb.append(" ");
                }
                in.set(i, sb.toString());
            }
        }

        // Build the grid
        Grid2DParser<Day22Tile> parser = new Grid2DParser<>(false, false, Day22Tile::byChar);
        parser.open(0);
        for (String s : in) {
            parser.read(0, s);
        }
        parser.close(0);

        Grid2D<Day22Tile> result = parser.getResult();
        result.addBorder(Day22Tile.OUT);
        return result;
    }

    private Coord2D findNeighbor(Grid2D<Day22Tile> map, Coord2D start, Coord2D toTest, Coord2D dir) {
        Coord2D next = new Coord2D((toTest.getX() + dir.getX()), (toTest.getY() + dir.getY()));
        LOG.debug("finding {} {} {} = {}", start, toTest, dir, next);
        return switch (map.get(next)) {
            case OPEN ->
                next;
            case WALL -> {
                hitWall = true;
                yield start;
            }
            case OUT -> {
                switch (dirPtr) {
                    case 0 -> {
                        if (next.getX() >= (map.getSize().getX() - 1)) {
                            next = new Coord2D(0, next.getY());
                        }
                    }
                    case 1 -> {
                        if (next.getY() >= (map.getSize().getY() - 1)) {
                            next = new Coord2D(next.getX(), 0);
                        }
                    }
                    case 2 -> {
                        if (next.getX() <= 0) {
                            next = new Coord2D((map.getSize().getX() - 1), next.getY());
                        }
                    }
                    case 3 -> {
                        if (next.getY() <= 0) {
                            next = new Coord2D(next.getX(), (map.getSize().getY() - 1));
                        }
                    }
                    default ->
                        throw Assert.failed(null, "Unexpected direction pointer");
                }
                yield findNeighbor(map, start, next, dir);
            }
        };
    }
}
