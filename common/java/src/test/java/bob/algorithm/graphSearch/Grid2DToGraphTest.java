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

public class Grid2DToGraphTest {

    public Grid2DToGraphTest() {
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
    public void testConvert() {
        // Build the grid
        Grid2DParser<Integer> parser = new Grid2DParser<>(false, false, c -> c - '0');
        parser.open(0);
        parser.read(0, "2294");
        parser.read(0, "2753");
        parser.read(0, "6183");
        parser.close(0);
        Grid2D<Integer> grid = parser.getResult();

        // Convert to a graph
        Graph<Coord2D, TestNode> graph = Grid2DToGraph.convert(TestNode.class, grid, (a, b) -> a - b);

        // Basic check of the generated graph
        assertAll("generated graph",
                  () -> assertEquals(12, graph.getNodeCt(), "Wrong number of nodes generated"),
                  () -> assertEquals(20, graph.getEdgeCt(), "Wrong number of edges generated")
        );
    }
}
