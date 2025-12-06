package bob.data.graph;

import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
    
    public static class TestNode implements Node<String> {

        @Getter
        private final String id;

        public TestNode(String id) {
            this.id = id;
        }
    }

    private Graph<String, TestNode> target;

    public GraphTest() {
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
    public void testAddEdgesUni() {
        target = new Graph<>(String.class, TestNode.class);

        target.addEdges(false, new String[]{"a", "b"});
        target.addEdges(false, new String[]{"b", "c", "d"});
        target.addEdges(false, new String[]{"a", "d"});
        
        assertEquals(4, target.getEdgeCt(), "wrong edge count");
        assertEquals(4, target.getNodeCt(), "wrong node count");
    }

    @Test
    public void testAddEdgesBi() {
        target = new Graph<>(String.class, TestNode.class);

        target.addEdges(true, new String[]{"a", "b"});
        target.addEdges(true, new String[]{"b", "c", "d"});
        target.addEdges(true, new String[]{"a", "d"});
        
        assertEquals(8, target.getEdgeCt(), "wrong edge count");
        assertEquals(4, target.getNodeCt(), "wrong node count");
    }

    @Test
    public void testGet() {
        target = new Graph<>(String.class, TestNode.class);

        target.addEdges(false, new String[]{"a", "b"});
        target.addEdges(false, new String[]{"b", "c", "d"});
        target.addEdges(false, new String[]{"a", "d"});
        
        assertAll("get returns value",
                () -> assertEquals("a", target.get("a").id, "a"),
                () -> assertEquals("b", target.get("b").id, "b"),
                () -> assertEquals("c", target.get("c").id, "c"),
                () -> assertEquals("d", target.get("d").id, "d")
        );
    }

    @Test
    public void testGetNeighborsUni() {
        target = new Graph<>(String.class, TestNode.class);

        target.addEdges(false, new String[]{"a", "b"});
        target.addEdges(false, new String[]{"b", "c", "d"});
        target.addEdges(false, new String[]{"a", "d"});
        
        List<TestNode> neighborsA = target.getNeighbors("a");
        assertAll("neighbors of a",
                () -> assertEquals(2, neighborsA.size(), "size"),
                () -> assertTrue(neighborsA.contains(target.get("b")), "contains b"),
                () -> assertTrue(neighborsA.contains(target.get("d")), "contains d")
        );
        List<TestNode> neighborsB = target.getNeighbors("b");
        assertAll("neighbors of b",
                () -> assertEquals(1, neighborsB.size(), "size"),
                () -> assertTrue(neighborsB.contains(target.get("c")), "contains c")
        );
        List<TestNode> neighborsC = target.getNeighbors("c");
        assertAll("neighbors of c",
                () -> assertEquals(1, neighborsC.size(), "size"),
                () -> assertTrue(neighborsC.contains(target.get("d")), "contains d")
        );
        List<TestNode> neighborsD = target.getNeighbors("d");
        assertAll("neighbors of d",
                () -> assertEquals(0, neighborsD.size(), "size")
        );
    }

    @Test
    public void testGetNeighborsBii() {
        target = new Graph<>(String.class, TestNode.class);

        target.addEdges(true, new String[]{"a", "b"});
        target.addEdges(true, new String[]{"b", "c", "d"});
        target.addEdges(true, new String[]{"a", "d"});
        
        List<TestNode> neighborsA = target.getNeighbors("a");
        assertAll("neighbors of a",
                () -> assertEquals(2, neighborsA.size(), "size"),
                () -> assertTrue(neighborsA.contains(target.get("b")), "contains b"),
                () -> assertTrue(neighborsA.contains(target.get("d")), "contains d")
        );
        List<TestNode> neighborsB = target.getNeighbors("b");
        assertAll("neighbors of b",
                () -> assertEquals(2, neighborsB.size(), "size"),
                () -> assertTrue(neighborsB.contains(target.get("a")), "contains a"),
                () -> assertTrue(neighborsB.contains(target.get("c")), "contains c")
        );
        List<TestNode> neighborsC = target.getNeighbors("c");
        assertAll("neighbors of c",
                () -> assertEquals(2, neighborsC.size(), "size"),
                () -> assertTrue(neighborsC.contains(target.get("b")), "contains b"),
                () -> assertTrue(neighborsC.contains(target.get("d")), "contains d")
        );
        List<TestNode> neighborsD = target.getNeighbors("d");
        assertAll("neighbors of d",
                () -> assertEquals(2, neighborsD.size(), "size"),
                () -> assertTrue(neighborsD.contains(target.get("a")), "contains a"),
                () -> assertTrue(neighborsD.contains(target.get("c")), "contains c")
        );
    }
}
