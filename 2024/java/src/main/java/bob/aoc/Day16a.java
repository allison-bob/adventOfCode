package bob.aoc;

import bob.algorithm.graphSearch.AStar;
import bob.algorithm.graphSearch.GraphSearchCache;
import bob.algorithm.graphSearch.GraphSearchNode;
import bob.data.coordinate.Coord2D;
import bob.data.coordinate.Mapper2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.Assert;
import bob.util.BaseClass;
import lombok.Getter;

public class Day16a extends BaseClass<Grid2D<Day16a.Spot>> {

    @Getter
    public enum Spot {
        EMPTY('.'),
        START('S'),
        END('E'),
        WALL('#');

        private final char symbol;

        private Spot(char symbol) {
            this.symbol = symbol;
        }

        public static Spot byChar(char in) {
            for (Spot at : Spot.values()) {
                if (at.symbol == in) {
                    return at;
                }
            }
            throw Assert.failed(null, "Unknown Spot symbol: " + in);
        }
    }

    @Getter
    public static class State extends GraphSearchNode<String> implements Comparable<State> {

        private final Coord2D loc;
        private final Coord2D dir;

        // Needed for graph
        public State(String id) {
            super(id);
            this.loc = new Coord2D(0, 0);
            this.dir = loc;
        }

        public State(Coord2D loc, Coord2D dir) {
            super(">" + dir + "@" + loc);
            this.loc = loc;
            this.dir = dir;
        }

        @Override
        public int compareTo(State that) {
            int result = this.loc.compareTo(that.loc);
            if (result == 0) {
                result = this.dir.compareTo(that.dir);
            }
            return result;
        }

        @Override
        public String toString() {
            return "[" + loc + ":" + dir + "]";
        }
    }

    private Grid2D<Spot> map;
    private Graph<String, State> graph;
    private State start;
    private State end;

    public static void main(String[] args) {
        new Day16a().run(args, "a");
    }

    public Day16a() {
        super(true);
        setParser(new Grid2DParser<>(false, false, Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Spot> data) {
        map = data;
        Coord2D mapSize = map.getSize();
        LOG.info("Read {} map", mapSize);

        // Scan the map to find start and end
        boolean looking = true;
        for (int y = 0; ((y < mapSize.getY()) && looking); y++) {
            for (int x = 0; ((x < mapSize.getX()) && looking); x++) {
                Spot c = map.get(x, y);
                if (c == Spot.START) {
                    start = new State(new Coord2D(x, y), new Coord2D(1, 0));
                    looking = (end == null);
                }
                if (c == Spot.END) {
                    end = new State(new Coord2D(x, y), new Coord2D(0, 0));
                    looking = (start == null);
                }
            }
        }
        LOG.debug("Start at {} and end at {}", start, end);

        // Initialize a graph with the starting and ending points
        graph = new Graph<>(String.class, State.class);
        graph.addNode(start);
        graph.addNode(end);
        LOG.debug("... graph contains {} nodes and {} edges", graph.getNodeCt(), graph.getEdgeCt());

        // Run the search to find the best path
        AStar<String, State> target = new AStar<>(graph);
        target.setNodeVisitor(this::visit);
        int result = target.findMinCost(start.getId(), end.getId());

        // Display result
        LOG.info("result is {}", result);
    }

    private void visit(GraphSearchCache<String> entry) {
        // Get the node for the current state
        State node = graph.get(entry.getId());

        // Update the cost-to-goal estimate
        entry.setEstCostToGoal(end.getLoc().manhattan(node.getLoc()));
        LOG.debug("Visiting {}", entry);

        // Do nothing if we are at the end
        if (entry.getEstCostToGoal() == 0) {
            LOG.debug("... at END");
            return;
        }

        // Add nodes for each possible next state
        State ahead = new State(node.getLoc().addOffset(node.getDir()), node.getDir());
        LOG.debug("... ahead is {}", map.get(ahead.getLoc()));
        if (map.get(ahead.getLoc()) != Spot.WALL) {
            if (map.get(ahead.getLoc()) == Spot.END) {
                ahead = end;
            } else if (graph.get(ahead.getId()) == null) {
                graph.addNode(ahead);
                LOG.debug("   ... adding node {}", ahead);
            }
            graph.addEdge(node.getId(), ahead.getId(), 1);
            LOG.debug("   ... adding edge {},{},{}", node.getId(), ahead.getId(), 1);
        }

        Coord2D left = Mapper2D.NW.map(node.getDir());
        LOG.debug("... left is {}", map.get(node.getLoc().addOffset(left)));
        if (map.get(node.getLoc().addOffset(left)) != Spot.WALL) {
            State n = new State(node.getLoc(), left);
            if (graph.get(n.getId()) == null) {
                graph.addNode(n);
                LOG.debug("   ... adding node {}", n);
            }
            graph.addEdge(node.getId(), n.getId(), 1000);
            LOG.debug("   ... adding edge {},{},{}", node.getId(), n.getId(), 1000);
        }

        Coord2D right = Mapper2D.SE.map(node.getDir());
        LOG.debug("... right is {}", map.get(node.getLoc().addOffset(right)));
        if (map.get(node.getLoc().addOffset(right)) != Spot.WALL) {
            State n = new State(node.getLoc(), right);
            if (graph.get(n.getId()) == null) {
                graph.addNode(n);
                LOG.debug("   ... adding node {}", n);
            }
            graph.addEdge(node.getId(), n.getId(), 1000);
            LOG.debug("   ... adding edge {},{},{}", node.getId(), n.getId(), 1000);
        }

        LOG.debug("... graph contains {} nodes and {} edges", graph.getNodeCt(), graph.getEdgeCt());
    }
}
