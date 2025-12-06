package bob.aoc;

import bob.algorithm.AStar;
import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day12b extends BaseClass<Grid2D<Day12Spot>> {

    public static void main(String[] args) {
        new Day12b().run(args, "");
    }

    public Day12b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day12Spot::new));
    }

    @Override
    public void solve(Grid2D<Day12Spot> data) {
        LOG.info("Read {} points", data.getSize());
        Day12Common.setIds(data);

        // Find the start and end
        List<Coord2D> start = data.pointStream()
                .filter(s -> (s.getInput() == 'S') || (s.getInput() == 'a'))
                .map(Day12Spot::getId)
                .collect(Collectors.toList());
        Coord2D end = data.pointStream()
                .filter(s -> s.getInput() == 'E')
                .findFirst().orElseThrow().getId();
        LOG.debug("Start at {} and end at {}", start, end);

        // Touch up the grid points
        Day12Common.setCostToGoal(data, end);
        for (Coord2D s : start) {
            data.get(s).setInput('a');
        }
        data.get(end).setInput('z');

        // Convert the grid to a graph
        Graph<Coord2D, Day12Spot> graph = Day12Common.gridToGraph(data);
        LOG.debug("Graph is {}", graph);

        // Run the algorithm to find the answer
        int minlen = Integer.MAX_VALUE;
        for (Coord2D s : start) {
            data.pointStream().forEach(sp -> {
                sp.setCostToHere(Integer.MAX_VALUE);
                sp.setVisited(false);
            });
            AStar<Coord2D, Day12Spot> algo = new AStar<>(graph);
            int len = algo.findPath(s, end);
            if (len < 1000) {
                minlen = Math.min(len, minlen);
            }
        }

        LOG.info("Answer is {}", minlen);
    }
}
