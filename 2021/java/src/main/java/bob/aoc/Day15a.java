package bob.aoc;

import bob.algorithm.AStar;
import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day15a extends BaseClass<Grid2D<Day15Node>> {

    public static void main(String[] args) {
        new Day15a().run(args, "");
    }

    public Day15a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day15Node::new));
    }

    @Override
    public void solve(Grid2D<Day15Node> data) {
        LOG.info("Read {} points", data.getSize());
        
        // Convert the grid to a graph
        Graph<Coord2D, Day15Node> graph = Day15Common.gridToGraph(data);
        Coord2D size = data.getSize();
        
        // Run the algorithm to find the answer
        AStar<Coord2D, Day15Node> algo = new AStar<>(graph);
        int risk = algo.findPath(new Coord2D(0, 0), new Coord2D((size.getX() - 1), (size.getY() - 1)));
        
        LOG.info("Answer is {}", risk);
    }
}
