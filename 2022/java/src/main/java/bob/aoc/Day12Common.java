package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day12Common {

    private static final Logger LOG = LoggerFactory.getLogger(Day12Common.class);

    private Day12Common() {
    }

    public static Graph<Coord2D, Day12Spot> gridToGraph(Grid2D<Day12Spot> in) {
        Graph<Coord2D, Day12Spot> retval = new Graph<>(Coord2D.class, Day12Spot.class);
        Coord2D size = in.getSize();

        // Load the grid points into the graph
        in.pointStream().forEach(retval::addNode);

        // Create the edges in the graph where the change is one step up
        for (int y = 0; y < size.getY(); y++) {
            for (int x = 0; x < size.getX(); x++) {
                Day12Spot curr = in.get(x, y);
                in.compassNeighborStream(curr.getId())
                        .filter(n -> n != null)
                        .filter(n -> ((curr.getInput() - n.getInput()) < 2))
                        .forEach(n -> retval.addEdge(n.getId(), curr.getId(), 1));
            }
        }

        return retval;
    }

    public static void setCostToGoal(Grid2D<Day12Spot> in, Coord2D end) {
        in.pointStream().forEach(s -> {
            s.setEstCostToGoal(s.getId().manhattan(end));
        });
    }

    public static void setIds(Grid2D<Day12Spot> in) {
        for (int y = 0; y < in.getSize().getY(); y++) {
            for (int x = 0; x < in.getSize().getX(); x++) {
                in.get(x, y).setId(new Coord2D(x, y));
            }
        }
    }
}
