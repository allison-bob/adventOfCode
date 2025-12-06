package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.parser.ObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day22b extends BaseClass<TwoFormatParser.Output<List<String>, List<List<Day22Instruction>>>> {

    private static final Pattern INSTR_PATTERN = Pattern.compile("[LR]");
    private static final Coord2D[] DIRECTIONS = new Coord2D[]{
        new Coord2D(1, 0), new Coord2D(0, 1), new Coord2D(-1, 0), new Coord2D(0, -1)
    };

    public static void main(String[] args) {
        new Day22b().run(args, "");
    }

    public Day22b() {
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
        
        Day22Mover mover = new Day22Mover(map, position, isRealData());
        
        for (Day22Instruction instr : instrs) {
            LOG.debug("Processing instruction {} at {}/{}", instr, mover.getPosition(), mover.getDirection());
            // Do the move
            mover.moveSteps(instr.getMove());
            LOG.debug("   Position now {}", mover.getPosition());

            // Do the turn
            mover.turn(instr.getTurn());
            LOG.debug("   Direction now {}", mover.getDirection());
        }
        
        int dir = -1;
        for (int i = 0; i < DIRECTIONS.length; i++) {
            if (DIRECTIONS[i].equals(mover.getDirection())) {
                dir = i;
            }
        }

        LOG.info("Answer is {}*1000 + {}*4 + {} = {}", mover.getPosition().getY(), mover.getPosition().getX(), dir,
                ((1000 * mover.getPosition().getY()) + (4 * mover.getPosition().getX()) + dir));
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
}
