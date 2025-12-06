package bob.data.grid;

import bob.data.coordinate.Coord1D;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Grid1DTest extends GridTestBase {

    private final Grid1D<GridTestEnum> target = new Grid1D<>();

    public Grid1DTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        for (List<GridTestEnum> r : testRows) {
            assertEquals(3, r.size());
        }
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

    private void loadGrid() {
        target.getPoints().clear();
        target.getPoints().addAll(testRows[0]);
    }

    // This method is used in many tests to validate that the grid dimensions are correct before
    // using loops to verify content at various coordinates
    private void checkSize(int x) {
        assertAll("grid size",
                () -> assertEquals(x, getGrid1Size(target), "X")
        );
    }

    @Test
    public void testAddBorder() {
        loadGrid();

        target.addBorder(GridTestEnum.BORDER);

        checkSize(5);
        assertEquals(GridTestEnum.BORDER, getGrid0(target, 0), "(0)");
        assertEquals(GridTestEnum.BORDER, getGrid0(target, 4), "(4)");
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(target, x + 1), "(" + (x + 1) + ")");
        }
    }

    @Test
    public void testAddRow() {
        target.addRow(testRows[0]);

        checkSize(3);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(target, x), "(" + x + ")");
        }
    }

    @Test
    public void testAddRowToFilled() {
        target.addRow(testRows[0]);
        target.addRow(testRows[1]);

        checkSize(3);
        for (int x = 0; x < testRows[1].size(); x++) {
            assertEquals(testRows[1].get(x), getGrid0(target, x), "(" + x + ")");
        }
    }

    @Test
    public void testAddRowL() {
        target.addRow(new Coord1D(0), testRows[0]);

        checkSize(3);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(target, x), "(" + x + ")");
        }
    }

    @Test
    public void testAddRowLToFilled() {
        target.addRow(new Coord1D(0), testRows[0]);
        target.addRow(new Coord1D(0), testRows[1]);

        checkSize(3);
        for (int x = 0; x < testRows[1].size(); x++) {
            assertEquals(testRows[1].get(x), getGrid0(target, x), "(" + x + ")");
        }
    }

    @Test
    public void testAddRowAtNonZero() {
        target.addRow(new Coord1D(20), testRows[0]);

        checkSize(3);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(target, x), "(" + x + ")");
        }
    }

    @Test
    public void testDump() {
        loadGrid();
        String expected = "ABC\n";

        String actual = target.dump(GridTestEnum::toString);

        assertEquals(expected, actual, "dump output");
    }

    @Test
    public void testGet() {
        loadGrid();

        checkSize(3);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), target.get(x), "(" + x + ")");
        }
    }

    @Test
    public void testGetOutside() {
        loadGrid();

        checkSize(3);
        assertAll("low coordinate",
                () -> assertNull(target.get(-1), "X")
        );
        assertAll("high coordinate",
                () -> assertNull(target.get(3), "X")
        );
    }

    @Test
    public void testGetSize() {
        loadGrid();
        checkSize(3);

        Coord1D size = target.getSize();

        assertAll("grid size",
                () -> assertEquals(3, size.getX(), "X")
        );
    }

    @Test
    public void testNeighborStream() {
        loadGrid();

        List<GridTestEnum> neighbors = target.neighborStream(new Coord1D(1))
                .collect(Collectors.toList());

        assertEquals(2, neighbors.size(), "count of neighbors");
        assertAll("neighbors",
                () -> assertTrue(neighbors.contains(GridTestEnum.A), "A"),
                () -> assertTrue(neighbors.contains(GridTestEnum.C), "C")
        );
    }

    @Test
    public void testPointStream() {
        loadGrid();

        List<GridTestEnum> points = target.pointStream()
                .collect(Collectors.toList());

        assertEquals(3, points.size(), "count of points");
        assertAll("points",
                () -> assertTrue(points.contains(GridTestEnum.A), "A"),
                () -> assertTrue(points.contains(GridTestEnum.B), "B"),
                () -> assertTrue(points.contains(GridTestEnum.C), "C")
        );
    }

    @Test
    public void testRowStream() {
        loadGrid();

        List<List<GridTestEnum>> rows = target.rowStream()
                .collect(Collectors.toList());

        assertEquals(1, rows.size(), "count of rows");
        List<GridTestEnum> points = rows.get(0);
        assertEquals(3, points.size(), "count of points");
        assertAll("points",
                () -> assertTrue(points.contains(GridTestEnum.A), "A"),
                () -> assertTrue(points.contains(GridTestEnum.B), "B"),
                () -> assertTrue(points.contains(GridTestEnum.C), "C")
        );
    }

    @Test
    public void testDirections() {
        loadGrid();

        List<Coord1D> dirs = target.directions()
                .distinct()
                .collect(Collectors.toList());

        assertEquals(3, dirs.size(), "count of distinct directions to neighbors");
    }
}
