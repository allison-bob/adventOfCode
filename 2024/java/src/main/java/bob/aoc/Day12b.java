package bob.aoc;

import bob.algorithm.graphSearch.BreadthFirst;
import bob.algorithm.graphSearch.Grid2DToGraph;
import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day12b extends BaseClass<Grid2D<Character>> {

    private static class Region {

        public Set<Coord2D> points = new HashSet<>();
        public int area;
        public int perimeter;
        public Character id;

        @Override
        public String toString() {
            return "{" + id + ":" + area + ":" + perimeter + ":" + points + "}";
        }
    }

    public static void main(String[] args) {
        new Day12b().run(args, "");
    }

    public Day12b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, c -> c));
    }

    @Override
    public void solve(Grid2D<Character> map) {
        Coord2D mapSize = map.getSize();
        LOG.info("Read {} map", mapSize);
        LOG.debug("Starting map:\n{}", map.dump(v -> v));

        // Convert to a graph
        Graph<Coord2D, Day12Node> graph = Grid2DToGraph.convert(Day12Node.class, map, this::edgeCost);
        //LOG.debug(graph.toString());

        // Create a list of all nodes
        Set<Coord2D> nodes = graph.nodeStream()
                .map(Day12Node::getId)
                .collect(Collectors.toSet());

        // Create regions until all nodes are in a region
        List<Region> regions = new ArrayList<>();
        BreadthFirst<Coord2D, Day12Node> searcher = new BreadthFirst<>(graph);
        while (!nodes.isEmpty()) {
            Region r = new Region();
            Coord2D start = nodes.iterator().next();
            r.id = map.get(start);
            r.points.addAll(searcher.findConnectedNodes(start));
            r.area = r.points.size();
            regions.add(r);
            nodes.removeAll(r.points);
        }

        // Calculate the perimeter length: The number of node edges equals the number of corners
        for (Region r : regions) {
            r.perimeter = 0; // should still be this, but to make sure...
            for (Coord2D p : r.points) {
                Character tgt = map.get(p);
                // Get the status of neighboring nodes (1 matches, 0 doesn't), array arrangement is:
                //     0 1 2
                //     3 . 4
                //     5 6 7
                Boolean[] stats = map.neighborStream(p)
                        .map(c -> !tgt.equals(c))
                        .toArray(Boolean[]::new);
                // Check each corner possibility
                if (stats[1] && stats[3]) {
                    r.perimeter++;
                }
                if (stats[1] && stats[4]) {
                    r.perimeter++;
                }
                if (stats[3] && stats[6]) {
                    r.perimeter++;
                }
                if (stats[4] && stats[6]) {
                    r.perimeter++;
                }
                if (!stats[1] && !stats[3] && stats[0]) {
                    r.perimeter++;
                }
                if (!stats[1] && !stats[4] && stats[2]) {
                    r.perimeter++;
                }
                if (!stats[3] && !stats[6] && stats[5]) {
                    r.perimeter++;
                }
                if (!stats[4] && !stats[6] && stats[7]) {
                    r.perimeter++;
                }
            }
        }
        LOG.debug("regions: {}", regions);

        // Find the total price
        long total = regions.stream()
                .mapToLong(r -> (r.area * r.perimeter))
                .sum();
        LOG.info("Total fence price is {}", total);
    }

    private int edgeCost(Character a, Character b) {
        if (a.equals(b)) {
            return 1;
        }
        return -1;
    }
}
