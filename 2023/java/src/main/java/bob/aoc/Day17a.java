package bob.aoc;

import bob.algorithm.AStar;
import bob.algorithm.DepthFirstSearch;
import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.UUID;

public class Day17a extends BaseClass<Grid2D<Integer>> {

    private Graph<Day17State, Day17Node> graph;
    private Grid2D<Integer> map;

    public static void main(String[] args) {
        new Day17a().run(args, "");
    }

    public Day17a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, c -> c - '0'));
    }

    @Override
    public void solve(Grid2D<Integer> data) {
        map = data;
        LOG.info("read {} grid", map.getSize());

        // Initialize the graph
        graph = new Graph<>(Day17State.class, Day17Node.class);
        Day17Node start = new Day17Node(map.getSize().addOffset(-1), graph, map);
        graph.addNode(start);

        // Find best result
        AStar<Day17State, Day17Node> search = new AStar<>(graph);
        int result = search.findPath(start.state, Day17Node::isDone);

        LOG.info("Total loss: {}", result);
    }
}
