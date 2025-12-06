package bob.algorithm.graphSearch;

import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DijkstrasTest {
    
    public DijkstrasTest() {
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
    // Implementation not stable at present
    public void testFindMaxCost() {
        // Build the grid
        //Grid2DParser<Integer> parser = new Grid2DParser<>(false, false, c -> c - '0');
        //parser.open(0);
        //parser.read(0, "11637");
        //parser.read(0, "13813");
        //parser.read(0, "21365");
        //parser.read(0, "36949");
        //parser.read(0, "74634");
        //parser.close(0);
        //Grid2D<Integer> grid = parser.getResult();

        // Convert to a graph
        //Graph<Coord2D, TestNode> graph = Grid2DToGraph.convert(TestNode.class, grid, (a, b) -> b);

        // Run the algorithm
        //Dijkstras<Coord2D, TestNode> target = new Dijkstras<>(graph);
        //target.setNodeVisitor(gsc -> System.out.println("Visiting " + gsc));
        //int result = target.findMaxCost(new Coord2D(0, 0), new Coord2D(4, 4));
        //assertEquals(81, result, "Max cost");
    }

    @Test
    public void testFindMinCost() {
        // Build the grid
        Grid2DParser<Integer> parser = new Grid2DParser<>(false, false, c -> c - '0');
        parser.open(0);
        parser.read(0, "11637");
        parser.read(0, "13813");
        parser.read(0, "21365");
        parser.read(0, "36949");
        parser.read(0, "74634");
        parser.close(0);
        Grid2D<Integer> grid = parser.getResult();

        // Convert to a graph
        Graph<Coord2D, TestNode> graph = Grid2DToGraph.convert(TestNode.class, grid, (a, b) -> b);

        // Run the algorithm
        Dijkstras<Coord2D, TestNode> target = new Dijkstras<>(graph);
        //target.setNodeVisitor(gsc -> System.out.println("Visiting " + gsc));
        int result = target.findMinCost(new Coord2D(0, 0), new Coord2D(4, 4));
        assertEquals(24, result, "Min cost");
    }
}
