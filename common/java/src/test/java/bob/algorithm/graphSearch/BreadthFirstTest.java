package bob.algorithm.graphSearch;

import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BreadthFirstTest {

    public BreadthFirstTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testFindAllEndNodes() {
        // Build the grid
        Grid2DParser<Integer> parser = new Grid2DParser<>(false, false, c -> c - '0');
        parser.open(0);
        parser.read(0, "99099");
        parser.read(0, "88188");
        parser.read(0, "43234");
        parser.read(0, "58885");
        parser.read(0, "68886");
        parser.close(0);
        Grid2D<Integer> grid = parser.getResult();

        // Convert to a graph
        Graph<Coord2D, TestNode> graph = Grid2DToGraph.convert(TestNode.class, grid, this::allEndNodesConverter);

        // Run the algorithm
        BreadthFirst<Coord2D, TestNode> target = new BreadthFirst<>(graph);
        //target.setNodeVisitor(gsc -> System.out.println("Visiting " + gsc));
        List<Coord2D> result = target.findAllEndNodes(new Coord2D(2, 0), c -> grid.get(c) == 6);
        assertEquals(2, result.size(), "Node count");
        assertTrue(result.contains(new Coord2D(0, 4)), "Node (0,4) not found");
        assertTrue(result.contains(new Coord2D(4, 4)), "Node (4,4) not found");
    }
    
    private int allEndNodesConverter(Integer a, Integer b) {
        int diff = b - a;
        if (diff == 1) {
            return 1;
        }
        return -1;
    }

    @Test
    public void testFindAllRoutesCount() {
        // Build the grid
        Grid2DParser<Integer> parser = new Grid2DParser<>(false, false, c -> c - '0');
        parser.open(0);
        parser.read(0, "8888808");
        parser.read(0, "8143218");
        parser.read(0, "8158828");
        parser.read(0, "8165438");
        parser.read(0, "8171148");
        parser.read(0, "8187658");
        parser.read(0, "8191118");
        parser.close(0);
        Grid2D<Integer> grid = parser.getResult();

        // Convert to a graph
        Graph<Coord2D, TestNode> graph = Grid2DToGraph.convert(TestNode.class, grid, this::allEndNodesConverter);

        // Run the algorithm
        BreadthFirst<Coord2D, TestNode> target = new BreadthFirst<>(graph);
        //target.setNodeVisitor(gsc -> System.out.println("Visiting " + gsc));
        int result = target.findAllRoutesCount(new Coord2D(5, 0), c -> grid.get(c) == 9);
        assertEquals(3, result, "Route count");
    }

    @Test
    public void testFindConnectedNodeCount() {
        // Build the grid
        Grid2DParser<Integer> parser = new Grid2DParser<>(false, false, c -> c - '0');
        parser.open(0);
        parser.read(0, "2199943210");
        parser.read(0, "3987894921");
        parser.read(0, "9856789892");
        parser.read(0, "8767896789");
        parser.read(0, "9899965678");
        parser.close(0);
        Grid2D<Integer> grid = parser.getResult();

        // Convert to a graph
        Graph<Coord2D, TestNode> graph = Grid2DToGraph.convert(TestNode.class, grid, this::connectedNodeConverter);

        // Run the algorithm
        BreadthFirst<Coord2D, TestNode> target = new BreadthFirst<>(graph);
        //target.setNodeVisitor(gsc -> System.out.println("Visiting " + gsc));
        int result = target.findConnectedNodeCount(new Coord2D(4, 2));
        assertEquals(14, result, "Node count");
    }
    
    private int connectedNodeConverter(Integer a, Integer b) {
        if (a > 8) {
            return -1;
        }
        if (b > 8) {
            return -1;
        }
        return 1;
    }

    @Test
    public void testFindConnectedNodes() {
        // Build the grid
        Grid2DParser<Integer> parser = new Grid2DParser<>(false, false, c -> c - '0');
        parser.open(0);
        parser.read(0, "2199943210");
        parser.read(0, "3987894921");
        parser.read(0, "9856789892");
        parser.read(0, "8767896789");
        parser.read(0, "9899965678");
        parser.close(0);
        Grid2D<Integer> grid = parser.getResult();

        // Convert to a graph
        Graph<Coord2D, TestNode> graph = Grid2DToGraph.convert(TestNode.class, grid, this::connectedNodeConverter);

        // Run the algorithm
        Set<Coord2D> expresult = Set.of(
                new Coord2D(2,1), new Coord2D(1,2), new Coord2D(0,3), new Coord2D(3,1), new Coord2D(2,2),
                new Coord2D(1,3), new Coord2D(3,2), new Coord2D(4,1), new Coord2D(2,3), new Coord2D(1,4),
                new Coord2D(4,2), new Coord2D(3,3), new Coord2D(4,3), new Coord2D(5,2)
        );
        BreadthFirst<Coord2D, TestNode> target = new BreadthFirst<>(graph);
        //target.setNodeVisitor(gsc -> System.out.println("Visiting " + gsc));
        Set<Coord2D> result = target.findConnectedNodes(new Coord2D(4, 2));
        assertEquals(expresult.size(), result.size(), "Node count");
        assertEquals(expresult, result, "Returned nodes");
    }

    @Test
    public void testFindShortestPathLength() {
        // Build the grid
        Grid2DParser<Character> parser = new Grid2DParser<>(false, false, c -> c);
        parser.open(0);
        parser.read(0, "Sabqponm");
        parser.read(0, "abcryxxl");
        parser.read(0, "accszExk");
        parser.read(0, "acctuvwj");
        parser.read(0, "abdefghi");
        parser.close(0);
        Grid2D<Character> grid = parser.getResult();
        Coord2D start = new Coord2D(0, 0);
        grid.set(start, 'a');
        Coord2D end = new Coord2D(5, 2);
        grid.set(end, 'z');

        // Convert to a graph
        Graph<Coord2D, TestNode> graph = Grid2DToGraph.convert(TestNode.class, grid, this::shortestPathLengthConverter);

        // Run the algorithm
        BreadthFirst<Coord2D, TestNode> target = new BreadthFirst<>(graph);
        //target.setNodeVisitor(gsc -> System.out.println("Visiting " + gsc));
        int result = target.findShortestPathLength(start, end);
        assertEquals(31, result, "Finished depth");
    }
    
    private int shortestPathLengthConverter(Character a, Character b) {
        int diff = b - a;
        if (diff < 2) {
            return 1;
        }
        return -1;
    }
}
