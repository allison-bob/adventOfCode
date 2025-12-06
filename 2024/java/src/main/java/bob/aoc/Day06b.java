package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.coordinate.Mapper2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Day06b extends BaseClass<Grid2D<Day06Spot>> {

    private class GuardState {

        public Coord2D location;
        public Coord2D direction;

        public GuardState() {
        }

        public GuardState(GuardState that) {
            this.location = that.location;
            this.direction = that.direction;
        }

        public Coord2D nextStep() {
            return new Coord2D((location.getX() + direction.getX()), (location.getY() + direction.getY()));
        }

        public void step() {
            location = nextStep();
        }

        public void turnRight() {
            direction = Mapper2D.NW.map(direction);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GuardState other = (GuardState) obj;
            if (!this.location.equals(other.location)) {
                return false;
            }
            return this.direction.equals(other.direction);
        }

        @Override
        public String toString() {
            return "Guard@" + location + " facing " + direction;
        }
    }

    private Grid2D<Day06Spot> initmap;
    private GuardState initguard;
    private Set<Coord2D> loops = new HashSet<>();
    private Set<Coord2D> exits = new HashSet<>();

    public static void main(String[] args) {
        new Day06b().run(args, "");
    }

    public Day06b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day06Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day06Spot> map) {
        initmap = map;
        Coord2D mapSize = map.getSize();
        LOG.info("Read {} map", mapSize);

        // Set up for operation
        GuardState guard = new GuardState();
        for (int y = 0; ((y < mapSize.getY()) && (guard.location == null)); y++) {
            for (int x = 0; ((x < mapSize.getX()) && (guard.location == null)); x++) {
                if (map.get(x, y) == Day06Spot.GUARD) {
                    guard.location = new Coord2D(x, y);
                }
            }
        }
        Assert.that((guard.location != null), "No guard found");
        guard.direction = new Coord2D(0, -1);
        initguard = new GuardState(guard);

        // Track the guard's path
        boolean result = guardWalk("M", initmap, initguard, this::loopCheck);
        Assert.that((result == false), "Main walk resulted in a loop!");

        LOG.info("could create {} loops", loops.size());
    }

    private boolean guardWalk(String prefix, Grid2D<Day06Spot> map, GuardState initguard, Consumer<Coord2D> checker) {
        GuardState guard = new GuardState(initguard);
        List<GuardState> obs = new ArrayList<>();
        LOG.debug("{} Walking from {}", prefix, guard);

        while (map.get(guard.location) != null) {
            if (map.get(guard.nextStep()) == Day06Spot.OBSTRUCTION) {
                LOG.debug("{} ... hit obstruction: {}", prefix, guard);
                while (map.get(guard.nextStep()) == Day06Spot.OBSTRUCTION) {
                    guard.turnRight();
                    //LOG.debug("{}     ... turned: {}", prefix, guard);
                }
                if (obs.contains(guard)) {
                    LOG.debug("{}     ... hit obstruction again: {}", prefix, guard);
                    return true;
                }
                obs.add(new GuardState(guard));
            }
            checker.accept(guard.nextStep());
            guard.step();
            //LOG.debug("{} ... step: {}", prefix, guard);
        }

        LOG.debug("{}     ... exited map: {}", prefix, guard);
        return false;
    }

    private void loopCheck(Coord2D toAdd) {
        // If the designated spot has already been checked, do nothing
        if (loops.contains(toAdd) || exits.contains(toAdd)) {
            return;
        }

        // Add an obstruction at the designated spot
        initmap.set(toAdd, Day06Spot.OBSTRUCTION);
        LOG.debug("\n\nMap to walk:\n{}", initmap.dump(Day06Spot::getSymbol));

        // Follow the guard until it leaves the map or creates a loop
        if (guardWalk("L", initmap, initguard, c -> {
              })) {
            loops.add(toAdd);
        } else {
            exits.add(toAdd);
        }

        // Clear the added obstruction at the designated spot
        initmap.set(toAdd, Day06Spot.EMPTY);
    }
}
