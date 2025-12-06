package bob.aoc;

import bob.data.coordinate.Coord3D;
import bob.data.grid.Grid2D;
import bob.data.grid.Grid3D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day17a extends BaseClass<Grid2D<Day17State>> {

    public static void main(String[] args) {
        new Day17a().run(args, "");
    }

    public Day17a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day17State::byChar));
    }

    @Override
    public void solve(Grid2D<Day17State> data) {
        Grid3D<Day17State> currState = new Grid3D<>();
        data.rowStream().forEach(currState::addRow);
        LOG.info("read {} rows of {} points", currState.getSize().getY(), currState.getSize().getX());
        LOG.debug("starting state:\n{}", currState.dump(p -> p.symbol));

        // Run the required cycles
        for (int i = 0; i < 6; i++) {
            currState.addBorder(Day17State.INACTIVE);

            Grid3D<Day17State> newState = new Grid3D<>();
            for (int z = 0; z < currState.getSize().getZ(); z++) {
                for (int y = 0; y < currState.getSize().getY(); y++) {
                    List<Day17State> row = new ArrayList<>();
                    for (int x = 0; x < currState.getSize().getX(); x++) {
                        Day17State here = currState.get(x, y, z);
                        //LOG.debug("n:{}", currState.neighborStream(x, y, z).collect(Collectors.toList()));
                        long active = currState.neighborStream(new Coord3D(x, y, z))
                                .filter(s -> s == Day17State.ACTIVE)
                                .count();
                        //LOG.debug("here({},{},{})={}, active={}", x, y, z, here, active);
                        if (here == Day17State.ACTIVE) {
                            if ((active < 2) || (active > 3)) {
                                //LOG.debug(" ... set inactive");
                                here = Day17State.INACTIVE;
                            }
                        } else {
                            if (active == 3) {
                                //LOG.debug(" ... set active");
                                here = Day17State.ACTIVE;
                            }
                        }
                        row.add(here);
                    }
                    newState.addRow(new Coord3D(0, y, z), row);
                }
            }

            currState = newState;
            LOG.debug("after {} cycles, system state:\n{}", (i + 1), currState.dump(p -> p.symbol));
        }

        long count = currState.pointStream()
                .filter(p -> p == Day17State.ACTIVE)
                .count();
        LOG.info("Active count is {}", count);
    }
}
