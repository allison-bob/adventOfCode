package bob.aoc;

import bob.algorithm.graphSearch.BreadthFirst;
import bob.algorithm.graphSearch.Grid2DToGraph;
import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.HashMap;
import java.util.Map;

public class Day10b extends BaseClass<Grid2D<Integer>> {

    public static void main(String[] args) {
        new Day10b().run(args, "");
    }

    public Day10b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, c -> c - '0'));
    }

    @Override
    public void solve(Grid2D<Integer> map) {
        Coord2D mapSize = map.getSize();
        LOG.info("Read {} map", mapSize);
        LOG.debug("Starting map:\n{}", map.dump(v -> v));
        
        // Convert to a graph
        Graph<Coord2D, Day10Node> graph = Grid2DToGraph.convert(Day10Node.class, map, this::edgeCost);
        LOG.debug(graph.toString());

        // Process all trailheads
        BreadthFirst<Coord2D, Day10Node> searcher = new BreadthFirst<>(graph);
        Map<Coord2D, Integer> endCounts = new HashMap<>();
        
        graph.nodeStream()
                .filter(n -> map.get(n.getId()) == 0)
                .forEach(n -> {
                    endCounts.put(n.getId(), searcher.findAllRoutesCount(n.getId(), c -> map.get(c) == 9));
                });
        LOG.debug("Counts: {}", endCounts);
        
        int total = endCounts.values().stream()
                .mapToInt(i -> i)
                .sum();
        LOG.info("Total trailhead score is {}", total);
    }
    
    private int edgeCost(Integer a, Integer b) {
        int diff = b - a;
        if (diff == 1) {
            return 1;
        }
        return -1;
    }
}
