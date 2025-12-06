package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.parser.LineObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Day15b extends BaseClass<TwoFormatParser.Output<Grid2D<Day15Spot>, List<List<Day15Direction>>>> {

    private static final Coord2D TO_LEFT = new Coord2D(-1, 0);
    private static final Coord2D TO_RIGHT = new Coord2D(1, 0);

    public static void main(String[] args) {
        new Day15b().run(args, "");
    }

    public Day15b() {
        super(false);
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
        LOG.debug("Starting map:\n{}", map.dump(Day15Spot::getSymbol));

        // Double-up the map
        Grid2D<Day15Spot> dblmap = new Grid2D<>();
        for (int y = 0; y < mapSize.getY(); y++) {
            List<Day15Spot> row = new ArrayList<>();
            for (int x = 0; x < mapSize.getX(); x++) {
                switch (map.get(x, y)) {
                    case BOX -> {
                        row.add(Day15Spot.BOX_LEFT);
                        row.add(Day15Spot.BOX_RIGHT);
                    }
                    case EMPTY -> {
                        row.add(Day15Spot.EMPTY);
                        row.add(Day15Spot.EMPTY);
                    }
                    case ROBOT -> {
                        row.add(Day15Spot.ROBOT);
                        row.add(Day15Spot.EMPTY);
                    }
                    case WALL -> {
                        row.add(Day15Spot.WALL);
                        row.add(Day15Spot.WALL);
                    }
                    default ->
                        throw Assert.failed(null, "Unexpected item in map at " + x + "," + y);
                }
            }
            dblmap.addRow(row);
        }
        map = dblmap;
        mapSize = map.getSize();
        LOG.debug("Doubled map:\n{}", map.dump(Day15Spot::getSymbol));

        // Find the robot
        Coord2D robot = null;
        for (int y = 0; ((y < mapSize.getY()) && (robot == null)); y++) {
            for (int x = 0; ((x < mapSize.getX()) && (robot == null)); x++) {
                if (map.get(x, y) == Day15Spot.ROBOT) {
                    robot = new Coord2D(x, y);
                }
            }
        }
        LOG.debug("Robot at {}", robot);

        // Step the robot through its moves
        for (Day15Direction d : directions) {
            robot = checkRobotMove(map, robot, d);
            LOG.debug("After {}:\n{}", d.getSymbol(), map.dump(Day15Spot::getSymbol));
        }

        // Find the coordinate total
        long total = 0;
        for (int y = 0; y < mapSize.getY(); y++) {
            for (int x = 0; x < mapSize.getX(); x++) {
                if (map.get(x, y) == Day15Spot.BOX_LEFT) {
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
            case BOX_LEFT -> {
                // See if the box can move
                int currpos = switch (d) {
                    case UP, DOWN ->
                        toMove.getY();
                    case LEFT, RIGHT ->
                        toMove.getX();
                };
                int newLimit = checkBoxMove(map, currpos, newpos, newpos.addOffset(TO_RIGHT), d);
                if (newLimit != currpos) {
                    startMoveBoxes(map, newLimit, toMove.addOffset(d.getOffset()), newpos.addOffset(d.getOffset()), d);
                }
                return moveRobot(map, toMove, newpos);
            }
            case BOX_RIGHT -> {
                // See if the box can move
                int currpos = switch (d) {
                    case UP, DOWN ->
                        toMove.getY();
                    case LEFT, RIGHT ->
                        toMove.getX();
                };
                int newLimit = checkBoxMove(map, currpos, newpos.addOffset(TO_LEFT), newpos, d);
                if (newLimit != currpos) {
                    startMoveBoxes(map, newLimit, toMove.addOffset(d.getOffset()), newpos.addOffset(d.getOffset()), d);
                }
                return moveRobot(map, toMove, newpos);
            }
            case EMPTY -> {
                return moveRobot(map, toMove, newpos);
            }
            default ->
                throw Assert.failed(null, "Unexpected item (" + dest + ") next to robot at " + newpos);
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

    private int checkBoxMove(Grid2D<Day15Spot> map, int limitSoFar, Coord2D toMoveL, Coord2D toMoveR, Day15Direction d) {
        LOG.debug("checkBoxMove(map,l={},tm={},{},d={})", limitSoFar, toMoveL, toMoveR, d);
        Coord2D newposL = toMoveL.addOffset(d.getOffset());
        Coord2D newposR = toMoveR.addOffset(d.getOffset());
        switch (d) {
            case LEFT -> {
                switch (map.get(newposL)) {
                    case WALL -> {
                        // Can't move
                        return limitSoFar;
                    }
                    case BOX_RIGHT -> {
                        // Check the neighboring box
                        return checkBoxMove(map, limitSoFar, newposL.addOffset(TO_LEFT), newposL, d);
                    }
                    case EMPTY -> {
                        // Can move
                        return newposL.getX();
                    }
                    default ->
                        throw Assert.failed(null, "Unexpected item in map at " + newposL);
                }
            }
            case RIGHT -> {
                switch (map.get(newposR)) {
                    case WALL -> {
                        // Can't move
                        return limitSoFar;
                    }
                    case BOX_LEFT -> {
                        // Check the neighboring box
                        return checkBoxMove(map, limitSoFar, newposR, newposR.addOffset(TO_RIGHT), d);
                    }
                    case EMPTY -> {
                        // Can move
                        return newposR.getX();
                    }
                    default ->
                        throw Assert.failed(null, "Unexpected item in map at " + newposR);
                }
            }
            case UP, DOWN -> {
                // Definitive cases
                if ((map.get(newposL) == Day15Spot.WALL) || (map.get(newposR) == Day15Spot.WALL)) {
                    return limitSoFar;
                }
                if ((map.get(newposL) == Day15Spot.EMPTY) && (map.get(newposR) == Day15Spot.EMPTY)) {
                    return newposL.getY();
                }
                // Cases that require more checks
                int toReturn = limitSoFar;
                BinaryOperator<Integer> cmp = Math::max;
                if (d == Day15Direction.UP) {
                    cmp = Math::min;
                }
                switch (map.get(newposL)) {
                    case BOX_LEFT -> {
                        // Check the neighboring box
                        int newLimit = checkBoxMove(map, limitSoFar, newposL, newposR, d);
                        //LOG.debug("... checkBoxMove returned {}", newLimit);
                        if (newLimit == limitSoFar) {
                            return limitSoFar;
                        }
                        toReturn = cmp.apply(newLimit, toReturn);
                        //LOG.debug("... toReturn is now {}", toReturn);
                    }
                    case BOX_RIGHT -> {
                        // Check the neighboring box
                        int newLimit = checkBoxMove(map, limitSoFar, newposL.addOffset(TO_LEFT), newposL, d);
                        //LOG.debug("... checkBoxMove returned {}", newLimit);
                        if (newLimit == limitSoFar) {
                            return limitSoFar;
                        }
                        toReturn = cmp.apply(newLimit, toReturn);
                        //LOG.debug("... toReturn is now {}", toReturn);
                    }
                }
                if (map.get(newposR) == Day15Spot.BOX_LEFT) {
                    // Check the neighboring box
                    int newLimit = checkBoxMove(map, limitSoFar, newposR, newposR.addOffset(TO_RIGHT), d);
                    //LOG.debug("... checkBoxMove returned {}", newLimit);
                    if (newLimit == limitSoFar) {
                        return limitSoFar;
                    }
                    toReturn = cmp.apply(newLimit, toReturn);
                    //LOG.debug("... toReturn is now {}", toReturn);
                }
                return toReturn;
            }
            default ->
                throw Assert.failed(null, "Unexpected direction " + d);
        }
    }

    private void startMoveBoxes(Grid2D<Day15Spot> map, int limit, Coord2D toMove, Coord2D newpos, Day15Direction d) {
        LOG.debug("startMoveBoxes(map,l={},tm={},np={},d={})", limit, toMove, newpos, d);
        switch (map.get(toMove)) {
            case BOX_LEFT ->
                moveBoxes(map, limit, toMove, toMove.addOffset(TO_RIGHT),
                        newpos, newpos.addOffset(TO_RIGHT), d);
            case BOX_RIGHT ->
                moveBoxes(map, limit, toMove.addOffset(TO_LEFT), toMove,
                        newpos.addOffset(TO_LEFT), newpos, d);
        }
    }

    private void moveBoxes(Grid2D<Day15Spot> map, int limit, Coord2D toMoveL, Coord2D toMoveR,
            Coord2D newposL, Coord2D newposR, Day15Direction d) {
        LOG.debug("moveBoxes(map,l={},tm={},{},np={},{},d={})", limit, toMoveL, toMoveR, newposL, newposR, d);
        switch (d) {
            case LEFT -> {
                switch (map.get(newposL)) {
                    case BOX_LEFT -> {
                        // Move the neighboring box
                        moveBoxes(map, limit, newposL, newposR,
                                newposL.addOffset(d.getOffset()),
                                newposR.addOffset(d.getOffset()), d);
                        // Move this box
                        moveBox(map, toMoveL, toMoveR, newposL, newposR, d);
                    }
                    case BOX_RIGHT -> {
                        // Move the neighboring box
                        moveBoxes(map, limit, newposL.addOffset(TO_LEFT), newposL,
                                newposL.addOffset(TO_LEFT).addOffset(d.getOffset()),
                                newposL.addOffset(d.getOffset()), d);
                        // Move this box
                        moveBox(map, toMoveL, toMoveR, newposL, newposR, d);
                    }
                    case EMPTY -> {
                        // Move this box
                        moveBox(map, toMoveL, toMoveR, newposL, newposR, d);
                    }
                    default ->
                        throw Assert.failed(null, "Unexpected item in map at " + newposL);
                }
            }
            case RIGHT -> {
                switch (map.get(newposR)) {
                    case BOX_LEFT -> {
                        // Move the neighboring box
                        moveBoxes(map, limit, newposR, newposR.addOffset(TO_RIGHT),
                                newposR.addOffset(TO_RIGHT),
                                newposR.addOffset(TO_RIGHT).addOffset(TO_RIGHT), d);
                        // Move this box
                        moveBox(map, toMoveL, toMoveR, newposL, newposR, d);
                    }
                    case EMPTY -> {
                        // Move this box
                        moveBox(map, toMoveL, toMoveR, newposL, newposR, d);
                    }
                    default ->
                        throw Assert.failed(null, "Unexpected item in map at " + newposR);
                }
            }
            case UP, DOWN -> {
                // Definitive cases
                if ((map.get(newposL) == Day15Spot.EMPTY) && (map.get(newposR) == Day15Spot.EMPTY)) {
                    moveBox(map, toMoveL, toMoveR, newposL, newposR, d);
                    return;
                }
                switch (map.get(newposL)) {
                    case BOX_LEFT -> {
                        if (toMoveL.getY() != limit) {
                            // Move the neighboring box
                            moveBoxes(map, limit, newposL, newposR,
                                    newposL.addOffset(d.getOffset()), newposR.addOffset(d.getOffset()), d);
                            // Move this box
                            moveBox(map, toMoveL, toMoveR, newposL, newposR, d);
                        }
                    }
                    case BOX_RIGHT -> {
                        if (toMoveL.getY() != limit) {
                            // Move the neighboring box
                            moveBoxes(map, limit, newposL.addOffset(TO_LEFT), newposL,
                                    newposL.addOffset(TO_LEFT).addOffset(d.getOffset()),
                                    newposL.addOffset(d.getOffset()), d);
                            // Move this box
                            moveBox(map, toMoveL, toMoveR, newposL, newposR, d);
                        }
                    }
                }
                if (map.get(newposR) == Day15Spot.BOX_LEFT) {
                    if (toMoveL.getY() != limit) {
                        // Move the neighboring box
                        moveBoxes(map, limit, newposR, newposR.addOffset(TO_RIGHT),
                                newposR.addOffset(d.getOffset()),
                                newposR.addOffset(TO_RIGHT).addOffset(d.getOffset()), d);
                        // Move this box
                        moveBox(map, toMoveL, toMoveR, newposL, newposR, d);
                    }
                }
            }
        }
    }

    private void moveBox(Grid2D<Day15Spot> map, Coord2D toMoveL, Coord2D toMoveR,
            Coord2D newposL, Coord2D newposR, Day15Direction d) {
        LOG.debug("moveBox(map,tm={},{},np={},{},d={})", toMoveL, toMoveR, newposL, newposR, d);
        if (((map.get(newposL) == Day15Spot.EMPTY) || (d == Day15Direction.RIGHT))
                && ((map.get(newposR) == Day15Spot.EMPTY) || (d == Day15Direction.LEFT))) {
            map.set(toMoveL, Day15Spot.EMPTY);
            map.set(toMoveR, Day15Spot.EMPTY);
            map.set(newposL, Day15Spot.BOX_LEFT);
            map.set(newposR, Day15Spot.BOX_RIGHT);
        }
    }
}
