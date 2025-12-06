package bob.aoc;

import bob.algorithm.DepthFirstSearch;
import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Day24a extends BaseClass<Grid2D<List<Day24Blizzard>>> {

    private Graph<UUID, Day24Node> graph;
    private final List<Grid2D<List<Day24Blizzard>>> maps = new ArrayList<>();
    private Coord2D start;
    private Coord2D finish;

    public static void main(String[] args) {
        new Day24a().run(args, "");
    }

    public Day24a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, this::parseChar));
    }

    private List<Day24Blizzard> parseChar(char c) {
        Day24Blizzard spot = Day24Blizzard.byChar(c);
        List<Day24Blizzard> val = new ArrayList<>();
        if (spot != Day24Blizzard.EMPTY) {
            val.add(spot);
        }
        return val;
    }

    @Override
    public void solve(Grid2D<List<Day24Blizzard>> valley) {
        LOG.info("Read {} valley", valley.getSize());
        maps.add(valley);

        // Find the start end finish coordinates
        int maxY = valley.getSize().getY() - 1;
        for (int x = 0; x < valley.getSize().getX(); x++) {
            if (valley.get(x, 0).isEmpty()) {
                start = new Coord2D(x, 0);
            }
            if (valley.get(x, maxY).isEmpty()) {
                finish = new Coord2D(x, maxY);
            }
        }

        // Initialize the graph
        graph = new Graph<>(UUID.class, Day24Node.class);
        Day24Node startPt = new Day24Node(start, 0, finish.manhattan(start));
        graph.addNode(startPt);

        // Find best result
        DepthFirstSearch<UUID, Day24Node> algo = new DepthFirstSearch<>(graph, this::visit, this::isDone);
        Day24Node result = algo.findMinCost(startPt);

        LOG.info("Answer is {}", result.getCostToHere());
    }

    private boolean isDone(Day24Node node) {
        return node.getExpedLoc().equals(finish);
    }

    private void visit(Day24Node node) {
        LOG.debug("Visiting {}", node);
        if (node.getDepth() > 300) {
            return;
        }
        
        // Make sure we have the map for the destination depth
        Assert.that((maps.size() > node.getDepth()), "depth increased without corresponding map");
        if (maps.size() == (node.getDepth() + 1)) {
            makeNextDepth();
        }
        
        // Check for potential moves
        Grid2D<List<Day24Blizzard>> curr = maps.get(node.getDepth() + 1);
        Coord2D exped = node.getExpedLoc();
        addState(curr, node, (exped.getX() + 0), (exped.getY() + 0));
        addState(curr, node, (exped.getX() + 0), (exped.getY() - 1));
        addState(curr, node, (exped.getX() + 0), (exped.getY() + 1));
        addState(curr, node, (exped.getX() - 1), (exped.getY() + 0));
        addState(curr, node, (exped.getX() + 1), (exped.getY() + 0));
    }

    private void addState(Grid2D<List<Day24Blizzard>> curr, Day24Node node, int newx, int newy) {
        List<Day24Blizzard> toCheck = curr.get(newx, newy);
        if ((toCheck != null) && toCheck.isEmpty()) {
            Coord2D newc = new Coord2D(newx, newy);
            Day24Node next = new Day24Node(newc, (node.getDepth() + 1), finish.manhattan(newc));
            graph.addNode(next);
            graph.addEdge(node.getId(), next.getId(), 0);
        }
    }

    private void makeNextDepth() {
        // Get the last map made so far
        Grid2D<List<Day24Blizzard>> prev = maps.get(maps.size() - 1);

        // Initialize a new map with all empty arrays
        Grid2D<List<Day24Blizzard>> next = new Grid2D<>();
        for (int y = 0; y < prev.getSize().getY(); y++) {
            List<List<Day24Blizzard>> row = new ArrayList<>();
            for (int x = 0; x < prev.getSize().getX(); x++) {
                row.add(new ArrayList<>());
            }
            next.addRow(row);
        }

        // Move all the items to their next location
        for (int y = 0; y < prev.getSize().getY(); y++) {
            for (int x = 0; x < prev.getSize().getX(); x++) {
                List<Day24Blizzard> prevloc = prev.get(x, y);
                for (Day24Blizzard b : prevloc) {
                    if (b == Day24Blizzard.WALL) {
                        next.get(x, y).add(b);
                    } else {
                        List<Day24Blizzard> nextloc = next.get(adjCoord(x, y, b.getMove(), next.getSize()));
                        nextloc.add(b);
                    }
                }
            }
        }
        
        // Add the new map to the list
        maps.add(next);
    }

    private Coord2D adjCoord(int x, int y, Coord2D move, Coord2D size) {
        int nx = x + move.getX();
        if (nx == 0) {
            nx = size.getX() - 2;
        } else if (nx == (size.getX() - 1)) {
            nx = 1;
        }

        int ny = y + move.getY();
        if (ny == 0) {
            ny = size.getY() - 2;
        } else if (ny == (size.getY() - 1)) {
            ny = 1;
        }
        
        return new Coord2D(nx, ny);
    }
}
