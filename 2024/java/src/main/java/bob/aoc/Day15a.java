package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.parser.LineObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.List;
import java.util.stream.Collectors;

public class Day15a extends BaseClass<TwoFormatParser.Output<Grid2D<Day15Spot>, List<List<Day15Direction>>>> {

    public static void main(String[] args) {
        new Day15a().run(args, "");
    }

    public Day15a() {
        super(true);
        setParser(new TwoFormatParser<>(
                new Grid2DParser<>(false, false, Day15Spot::byChar),
                new LineObjectParser<>(Day15Direction::byChar)
        ));
    }

    @Override
    public void solve(TwoFormatParser.Output<Grid2D<Day15Spot>, List<List<Day15Direction>>> data) {
        Grid2D<Day15Spot> map = data.first;
        List<Day15Direction> directions = data.rest.stream()
                .flatMap(l -> l.stream())
                .collect(Collectors.toList());
        Coord2D mapSize = map.getSize();
        LOG.info("Read {} map and {} directions", mapSize, directions.size());
        //LOG.debug("Starting map:\n{}", map.dump(Day15Spot::getSymbol));

        // Find the robot
        Coord2D robot = null;
        for (int y = 0; ((y < mapSize.getY()) && (robot == null)); y++) {
            for (int x = 0; ((x < mapSize.getX()) && (robot == null)); x++) {
                if (map.get(x, y) == Day15Spot.ROBOT) {
                    robot = new Coord2D(x, y);
                }
            }
        }

        // Step the robot through its moves
        for (Day15Direction d : directions) {
            robot = checkRobotMove(map, robot, d);
        }

        // Find the coordinate total
        long total = 0;
        for (int y = 0; y < mapSize.getY(); y++) {
            for (int x = 0; x < mapSize.getX(); x++) {
                if (map.get(x, y) == Day15Spot.BOX) {
                    total += (100 * y) + x;
                }
            }
        }
        LOG.info("Total GPS coordinates is {}", total);
    }

    private Coord2D checkRobotMove(Grid2D<Day15Spot> map, Coord2D toMove, Day15Direction d) {
        Coord2D newpos = toMove.addOffset(d.getOffset());
        Day15Spot dest = map.get(newpos);
        switch (dest) {
            case WALL -> {
                // Can't move
                return toMove;
            }
            case BOX -> {
                // See if the box can move
                checkBoxMove(map, newpos, d);
                return moveRobot(map, toMove, newpos);
            }
            case EMPTY -> {
                return moveRobot(map, toMove, newpos);
            }
            default -> throw Assert.failed(null, "Unexpected item (" + dest + ") next to robot at " + newpos);
        }
    }

    private Coord2D moveRobot(Grid2D<Day15Spot> map, Coord2D toMove, Coord2D newpos) {
        if (map.get(newpos) == Day15Spot.EMPTY) {
            map.set(toMove, Day15Spot.EMPTY);
            map.set(newpos, Day15Spot.ROBOT);
            return newpos;
        }
        return toMove;
    }

    private void checkBoxMove(Grid2D<Day15Spot> map, Coord2D toMove, Day15Direction d) {
        Coord2D newpos = toMove.addOffset(d.getOffset());
        Day15Spot dest = map.get(newpos);
        switch (dest) {
            case WALL -> {
            }
            case BOX -> {
                // See if the neighboring box can move
                checkBoxMove(map, newpos, d);
                moveBox(map, toMove, newpos);
            }
            case EMPTY -> {
                moveBox(map, toMove, newpos);
            }
            default -> throw Assert.failed(null, "Unexpected item (" + dest + ") next to box at " + newpos);
        }
    }

    private void moveBox(Grid2D<Day15Spot> map, Coord2D toMove, Coord2D newpos) {
        if (map.get(newpos) == Day15Spot.EMPTY) {
            map.set(toMove, Day15Spot.EMPTY);
            map.set(newpos, Day15Spot.BOX);
        }
    }
}
