package bob.algorithm.graphSearch;

import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DepthFirstTest {
    
    public DepthFirstTest() {
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
        DepthFirst<Coord2D, TestNode> target = new DepthFirst<>(graph);
        int result = target.findAllRoutesCount(new Coord2D(5, 0), c -> grid.get(c) == 9);
        assertEquals(3, result, "Route count");
    }
    
    private int allEndNodesConverter(Integer a, Integer b) {
        int diff = b - a;
        if (diff == 1) {
            return 1;
        }
        return -1;
    }

    @Test
    public void testFindBestRoutesCount() {
        // Build the grid
        Grid2DParser<Integer> parser = new Grid2DParser<>(false, false, c -> c - '0');
        parser.open(0);
        parser.read(0, "19999999");
        parser.read(0, "19999999");
        parser.read(0, "11111119");
        parser.read(0, "29929939");
        parser.read(0, "29929939");
        parser.read(0, "11111119");
        parser.read(0, "99999911");
        parser.read(0, "99999991");
        parser.close(0);
        Grid2D<Integer> grid = parser.getResult();

        // Convert to a graph
        Graph<Coord2D, TestNode> graph = Grid2DToGraph.convert(TestNode.class, grid, (a,b) -> {
            if (b > 5) {
                return -1;
            }
            return b;
        });

        // Run the algorithm
        DepthFirst<Coord2D, TestNode> target = new DepthFirst<>(graph);
        target.setNodeVisitor(gsc -> System.out.println("      Visit " + gsc));
        Map.Entry<Integer, Integer> result = target.findBestRoutesCount(new Coord2D(0, 0), new Coord2D(7, 7));
        //assertEquals(16, result.getKey(), "Route cost");
        //assertEquals(2, result.getValue(), "Route count");
    }
}
