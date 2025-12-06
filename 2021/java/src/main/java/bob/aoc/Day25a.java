package bob.aoc;

import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day25a extends BaseClass<Grid2D<Day25Spot>> {
    
    private Grid2D<Day25Spot> currState;

    public static void main(String[] args) {
        new Day25a().run(args, "");
    }

    public Day25a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day25Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day25Spot> map) {
        LOG.info("read {} spots", map.getSize());
        currState = map;
        
        boolean changes = true;
        int i;
        for (i = 0; ((i < 1000) && changes); i++) {
            changes = doStep();
            LOG.debug("After {} steps:\n{}", (i + 1), currState.dump(Day25Spot::getSymbol));
        }

        LOG.info("Cucumbers stop after {} steps", i);
    }
    
    private boolean doStep() {
        boolean changes = false;
        
        Day25Spot[][] afterEast = new Day25Spot[currState.getSize().getY()][currState.getSize().getX()];
        for (int y = 0; y < currState.getSize().getY(); y++) {
            for (int x = 0; x < currState.getSize().getX(); x++) {
                if (afterEast[y][x] == null) {
                    switch (currState.get(x, y)) {
                        case EMPTY -> afterEast[y][x] = Day25Spot.EMPTY;
                        case SOUTH -> afterEast[y][x] = Day25Spot.SOUTH;
                        case EAST -> {
                            int newx = (x == (currState.getSize().getX() - 1)) ? 0 : (x + 1);
                            if (currState.get(newx, y) == Day25Spot.EMPTY) {
                                afterEast[y][x] = Day25Spot.EMPTY;
                                afterEast[y][newx] = Day25Spot.EAST;
                                changes = true;
                            } else {
                                afterEast[y][x] = Day25Spot.EAST;
                            }
                        }
                    }
                }
            }
        }
        
        Day25Spot[][] afterSouth = new Day25Spot[currState.getSize().getY()][currState.getSize().getX()];
        for (int y = 0; y < currState.getSize().getY(); y++) {
            for (int x = 0; x < currState.getSize().getX(); x++) {
                if (afterSouth[y][x] == null) {
                    switch (afterEast[y][x]) {
                        case EMPTY -> afterSouth[y][x] = Day25Spot.EMPTY;
                        case EAST -> afterSouth[y][x] = Day25Spot.EAST;
                        case SOUTH -> {
                            int newy = (y == (currState.getSize().getY() - 1)) ? 0 : (y + 1);
                            if (afterEast[newy][x] == Day25Spot.EMPTY) {
                                afterSouth[y][x] = Day25Spot.EMPTY;
                                afterSouth[newy][x] = Day25Spot.SOUTH;
                                changes = true;
                            } else {
                                afterSouth[y][x] = Day25Spot.SOUTH;
                            }
                        }
                    }
                }
            }
        }
        
        currState = new Grid2D<>();
        for (int i = 0; i < afterSouth.length; i++) {
            currState.addRow(Arrays.asList(afterSouth[i]));
        }
        return changes;
    }
}
