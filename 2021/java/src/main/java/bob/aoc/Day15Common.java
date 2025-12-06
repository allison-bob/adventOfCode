package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;

public class Day15Common {

    private Day15Common() {
    }

    public static Graph<Coord2D, Day15Node> gridToGraph(Grid2D<Day15Node> in) {
        Graph<Coord2D, Day15Node> retval = new Graph<>(Coord2D.class, Day15Node.class);
        Coord2D size = in.getSize();
        
        // Update the grid points
        for (int y = 0; y < size.getY(); y++) {
            for (int x = 0; x < size.getX(); x++) {
                in.get(x, y).setPosition(x, y, (size.getX() - 1), (size.getY() - 1));
            }
        }
        
        // Load the grid points into the graph
        in.pointStream().forEach(retval::addNode);
        
        // Create the edges in the graph; we need to create two edges between points to account for
        // the different cost in each direction (cost is based on the point being entered)
        for (int y = 0; y < size.getY(); y++) {
            for (int x = 0; x < size.getX(); x++) {
                Day15Node curr = in.get(x, y);
                in.compassNeighborStream(curr.getId())
                        .filter(n -> n != null)
                        .forEach(n -> retval.addEdge(n.getId(), curr.getId(), curr.getInput()));
            }
        }
        
        return retval;
    }
}
