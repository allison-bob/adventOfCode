package bob.aoc;

import bob.algorithm.AStar;
import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;

public class Day12a extends BaseClass<Grid2D<Day12Spot>> {

    public static void main(String[] args) {
        new Day12a().run(args, "");
    }

    public Day12a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day12Spot::new));
    }

    @Override
    public void solve(Grid2D<Day12Spot> data) {
        LOG.info("Read {} points", data.getSize());
        Day12Common.setIds(data);

        // Find the start and end
        Coord2D start = data.pointStream()
                .filter(s -> s.getInput() == 'S')
                .findFirst().orElseThrow().getId();
        Coord2D end = data.pointStream()
                .filter(s -> s.getInput() == 'E')
                .findFirst().orElseThrow().getId();
        LOG.debug("Start at {} and end at {}", start, end);

        // Touch up the grid points
        Day12Common.setCostToGoal(data, end);
        data.get(start).setInput('a');
        data.get(end).setInput('z');

        // Convert the grid to a graph
        Graph<Coord2D, Day12Spot> graph = Day12Common.gridToGraph(data);
        LOG.debug("Graph is {}", graph);

        // Run the algorithm to find the answer
        AStar<Coord2D, Day12Spot> algo = new AStar<>(graph);
        int len = algo.findPath(start, end);

        LOG.info("Answer is {}", len);
    }
}
