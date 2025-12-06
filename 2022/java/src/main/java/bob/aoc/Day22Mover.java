package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.coordinate.Mapper2D;
import bob.data.grid.Grid2D;
import bob.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day22Mover {
    
    private static class State {
        public Coord2D position;
        public Coord2D direction;

        public State(Coord2D position, Coord2D direction) {
            this.position = position;
            this.direction = direction;
        }
        
        public State next() {
            return new State(new Coord2D((position.getX() + direction.getX()), (position.getY() + direction.getY())),
                    direction);
        }

        @Override
        public String toString() {
            return "" + position + "/" + direction;
        }
    }

    private static final Coord2D[] DIRECTIONS = new Coord2D[]{
        new Coord2D(1, 0), new Coord2D(0, 1), new Coord2D(-1, 0), new Coord2D(0, -1)
    };
    private static final Coord2D C_1000200 = new Coord2D(-100, 200);
    private static final Coord2D C_1000201 = new Coord2D(-100, 201);
    private static final Coord2D C_0500051 = new Coord2D(-50, 51);
    private static final Coord2D C_005_004 = new Coord2D(-5, -4);
    private static final Coord2D C00000101 = new Coord2D(0, 101);
    private static final Coord2D C00000151 = new Coord2D(0, 151);
    private static final Coord2D C00080004 = new Coord2D(8, 4);
    private static final Coord2D C00090013 = new Coord2D(9, 13);
    private static final Coord2D C00130008 = new Coord2D(13, 8);
    private static final Coord2D C0050_051 = new Coord2D(50, -51);
    private static final Coord2D C00500050 = new Coord2D(50, 50);
    private static final Coord2D C00500100 = new Coord2D(50, 100);
    private static final Coord2D C0051_151 = new Coord2D(51, -151);
    private static final Coord2D C00510151 = new Coord2D(51, 151);
    private static final Coord2D C0100_201 = new Coord2D(100, -201);
    private static final Coord2D C01000000 = new Coord2D(100, 0);
    private static final Coord2D C01010051 = new Coord2D(101, 51);
    private static final Coord2D C01010101 = new Coord2D(101, 101);
    private static final Coord2D C01510050 = new Coord2D(151, 50);

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final Grid2D<Day22Tile> map;
    private State current;
    private final boolean realData;
    private boolean hitWall;

    public Day22Mover(Grid2D<Day22Tile> map, Coord2D position, boolean realData) {
        this.map = map;
        this.current = new State(position, new Coord2D(1, 0));
        this.realData = realData;
    }
    
    public Coord2D getDirection() {
        return current.direction;
    }
    
    public Coord2D getPosition() {
        return current.position;
    }

    public void moveSteps(int ct) {
        // Reset status variables
        hitWall = false;
        
        // Do each step
        for (int i = 0; ((i < ct) && (!hitWall)); i++) {
            current = findNeighbor(current);
        }
    }
    
    public void turn(int dir) {
        switch (dir) {
            case 0 -> {}
            case 1 -> current = new State(current.position, Mapper2D.NW.map(current.direction));
            case 3 -> current = new State(current.position, Mapper2D.SE.map(current.direction));
            default -> throw Assert.failed(null, "Unexpected turn direction " + dir);
        }
    }

    private State findNeighbor(State toTest) {
        State next = toTest.next();
        LOG.debug("finding {} -> {}", toTest, next);
        return switch (map.get(next.position)) {
            case OPEN -> {
                yield next;
            }
            case WALL -> {
                hitWall = true;
                yield current;
            }
            case OUT -> {
                if (realData) {
                    yield wrapReal(next);
                } else {
                    yield wrapSample(next);
                }
            }
        };
    }
    
    private State move(State from, Coord2D offset) {
        return new State(new Coord2D((from.position.getX() + offset.getX()), (from.position.getY() + offset.getY())),
                from.direction);
    }
    
    private State pivot(State from, Mapper2D mapper, Coord2D origin) {
        Coord2D moved = new Coord2D((from.position.getX() - origin.getX()), (from.position.getY() - origin.getY()));
        Coord2D spun = mapper.map(moved);
        moved = new Coord2D((spun.getX() + origin.getX()), (spun.getY() + origin.getY()));
        LOG.debug("     pivoted to {}", moved);
        return new State(moved, mapper.map(from.direction));
    }
    
    private boolean testXBoundary(State toTest, int x, int dx, int yLo, int yHi) {
        return (toTest.position.getX() == x) && (toTest.direction.getX() == dx)
                && (toTest.position.getY() >= yLo) && (toTest.position.getY() <= yHi);
    }
    
    private boolean testYBoundary(State toTest, int y, int dy, int xLo, int xHi) {
        return (toTest.position.getY() == y) && (toTest.direction.getY() == dy)
                && (toTest.position.getX() >= xLo) && (toTest.position.getX() <= xHi);
    }

    private State wrapReal(State next) {
        if (testXBoundary(next, 0, -1, 101, 150)) {
            // Left to Back
            return findNeighbor(move(pivot(next, Mapper2D.WS, C00000101), C0050_051));
        }
        if (testXBoundary(next, 0, -1, 151, 200)) {
            // Down to Back
            return findNeighbor(move(pivot(next, Mapper2D.SE, C00000151), C0051_151));
        }
        if (testXBoundary(next, 50, -1, 1, 50)) {
            // Back to Left
            return findNeighbor(move(pivot(next, Mapper2D.WS, C00500050), C_0500051));
        }
        if (testXBoundary(next, 50, -1, 51, 100)) {
            // Up to Left
            return findNeighbor(pivot(next, Mapper2D.SE, C00500100));
        }
        if (testXBoundary(next, 51, 1, 151, 200)) {
            // Down to Front
            return findNeighbor(pivot(next, Mapper2D.SE, C00510151));
        }
        if (testXBoundary(next, 101, 1, 51, 100)) {
            // Up to Right
            return findNeighbor(pivot(next, Mapper2D.SE, C01010051));
        }
        if (testXBoundary(next, 101, 1, 101, 150)) {
            // Front to Right
            return findNeighbor(move(pivot(next, Mapper2D.WS, C01010101), C0050_051));
        }
        if (testXBoundary(next, 151, 1, 1, 50)) {
            // Right to Front
            return findNeighbor(move(pivot(next, Mapper2D.WS, C01510050), C_0500051));
        }
        if (testYBoundary(next, 0, -1, 51, 100)) {
            // Back to Down
            return findNeighbor(move(pivot(next, Mapper2D.NW, C01000000), C_1000200));
        }
        if (testYBoundary(next, 0, -1, 101, 150)) {
            // Right to Down
            return findNeighbor(move(next, C_1000201));
        }
        if (testYBoundary(next, 51, 1, 101, 150)) {
            // Right to Up
            return findNeighbor(pivot(next, Mapper2D.NW, C01010051));
        }
        if (testYBoundary(next, 100, -1, 1, 50)) {
            // Left to Up
            return findNeighbor(pivot(next, Mapper2D.NW, C00500100));
        }
        if (testYBoundary(next, 151, 1, 51, 100)) {
            // Front to Down
            return findNeighbor(pivot(next, Mapper2D.NW, C00510151));
        }
        if (testYBoundary(next, 201, 1, 1, 50)) {
            // Down to Right
            return findNeighbor(move(next, C0100_201));
        }
        throw Assert.failed(null, "Unexpected OUT: " + next);
        /*
        if ((next.getY() == 201) && (newDirPtr == 1)) {
            if ((next.getX() > 0) && (next.getX() < 51)) {
                // Down to Right: x=100+(x-0), y=0
                newDirPtr = 1;
                return findNeighbor(map, start, new Coord2D((next.getX() + 100), 0), DIRECTIONS[newDirPtr]);
            }
        }
        */
    }

    private State wrapSample(State next) {
        if (testXBoundary(next, 13, 1, 5, 8)) {
            // Front to Right
            return findNeighbor(pivot(next, Mapper2D.NW, C00130008));
        }
        if (testYBoundary(next, 4, -1, 5, 8)) {
            // Left to Up
            return findNeighbor(pivot(next, Mapper2D.NW, C00080004));
        }
        if (testYBoundary(next, 13, 1, 9, 12)) {
            // Down to Back
            return findNeighbor(move(pivot(next, Mapper2D.WS, C00090013), C_005_004));
        }
        throw Assert.failed(null, "Unexpected OUT: " + next);
    }
}
