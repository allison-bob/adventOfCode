package bob.data.grid;

import bob.data.coordinate.Coord2D;
import bob.data.coordinate.Mapper2D;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class Grid2DTest extends GridTestBase {

    private Grid2D<GridTestEnum> target = new Grid2D<>();

    public Grid2DTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        for (List<GridTestEnum> r : testRows) {
            assertEquals(3, r.size());
        }
    }

    @AfterEach
    public void tearDown() {
    }

    private void loadGrid() {
        target.getSubgrids().clear();
        for (List<GridTestEnum> row : testRows2D) {
            target.getSubgrids().add(loadGrid1(row));
        }
    }

    // This method is used in many tests to validate that the grid dimensions are correct before
    // using loops to verify content at various coordinates
    private void checkSize(int x, int y) {
        assertAll("grid size",
                () -> assertEquals(y, getGrid2Size(target), "Y"),
                () -> assertEquals(x, getGrid1Size(getGrid1(target, 0)), "X")
        );
    }

    @Test
    public void testAddBorder() {
        loadGrid();

        target.addBorder(GridTestEnum.BORDER);

        checkSize(5, 5);
        // Borders added to existing subgrids is tested in the down-dimension test, just need to verify here
        // that the new subgrids were added correctly
        Grid1D<GridTestEnum> bgy0 = getGrid1(target, 0);
        Grid1D<GridTestEnum> bgy4 = getGrid1(target, 4);
        for (int x = 0; x < getGrid1Size(bgy0); x++) {
            assertEquals(GridTestEnum.BORDER, getGrid0(bgy0, x), "(" + x + ",0)");
            assertEquals(GridTestEnum.BORDER, getGrid0(bgy4, x), "(" + x + ",4)");
        }
    }

    @Test
    public void testAddRowEmpty() {
        target.addRow(testRows[0]);

        checkSize(3, 1);
        Grid1D<GridTestEnum> g0 = getGrid1(target, 0);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(g0, x), "(" + x + ",0)");
        }
    }

    @Test
    public void testAddRowNotEmpty() {
        target.addRow(testRows[0]);
        target.addRow(testRows[1]);

        checkSize(3, 2);
        Grid1D<GridTestEnum> g0 = getGrid1(target, 0);
        Grid1D<GridTestEnum> g1 = getGrid1(target, 1);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(g0, x), "(" + x + ",0)");
            assertEquals(testRows[1].get(x), getGrid0(g1, x), "(" + x + ",1)");
        }
    }

    @Test
    public void testAddRowLNegative() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> target.addRow(new Coord2D(0, -3), testRows[0]));
        assertTrue(thrown.getMessage().contains("Negative"), "Unexpected exception message");
    }

    @Test
    public void testAddRowLEmptyBadCoord() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> target.addRow(new Coord2D(0, 1), testRows[0]));
        assertTrue(thrown.getMessage().contains("first row"), "Unexpected exception message");
    }

    @Test
    public void testAddRowLNotEmptyBadCoord() {
        target.addRow(new Coord2D(0, 0), testRows[0]);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> target.addRow(new Coord2D(0, 2), testRows[0]));
        assertTrue(thrown.getMessage().contains("too large"), "Unexpected exception message");
    }

    @Test
    public void testAddRowLEmpty() {
        target.addRow(new Coord2D(0, 0), testRows[0]);

        checkSize(3, 1);
        Grid1D<GridTestEnum> g0 = getGrid1(target, 0);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(g0, x), "(" + x + ",0)");
        }
    }

    @Test
    public void testAddRowLNotEmpty() {
        target.addRow(new Coord2D(0, 0), testRows[0]);
        target.addRow(new Coord2D(0, 1), testRows[1]);

        checkSize(3, 2);
        Grid1D<GridTestEnum> g0 = getGrid1(target, 0);
        Grid1D<GridTestEnum> g1 = getGrid1(target, 1);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(g0, x), "(" + x + ",0)");
            assertEquals(testRows[1].get(x), getGrid0(g1, x), "(" + x + ",1)");
        }
    }

    @Test
    public void testDump() {
        loadGrid();
        String expected = "ABC\nDEF\nGHI\n";

        String actual = target.dump(GridTestEnum::toString);

        assertEquals(expected, actual, "dump output");
    }

    @ParameterizedTest
    @EnumSource
    public void testMappingForNull(Mapper2D orient) {
        loadGrid();

        target.setOrientation(orient);
        long nullct = target.pointStream()
                .filter(gte -> gte == null)
                .count();
        
        assertEquals(0, nullct, "Orientation " + orient + " produced nulls:\n" + target.dump(p -> p));
    }

    @Test
    public void testGet() {
        loadGrid();

        checkSize(3, 3);
        for (int y = 0; y < testRows2D.length; y++) {
            List<GridTestEnum> row = testRows2D[y];
            for (int x = 0; x < row.size(); x++) {
                assertEquals(row.get(x), target.get(x, y), "(" + x + "," + y + ")");
            }
        }
    }

    @Test
    public void testGetOutside() {
        loadGrid();

        checkSize(3, 3);
        assertAll("low coordinate",
                () -> assertNull(target.get(-1, 0), "X"),
                () -> assertNull(target.get(0, -1), "Y")
        );
        assertAll("high coordinate",
                () -> assertNull(target.get(3, 0), "X"),
                () -> assertNull(target.get(0, 3), "Y")
        );
    }

    @Test
    public void testGetMapped() {
        loadGrid();
        target.setOrientation(Mapper2D.NE);

        checkSize(3, 3);
        for (int y = 0; y < testRows2D.length; y++) {
            List<GridTestEnum> row = testRows2D[y];
            for (int x = 0; x < row.size(); x++) {
                assertEquals(row.get(x), target.get(y, x), "(" + y + "," + x + ")");
            }
        }
    }

    @Test
    public void testGetWrappedMapped() {
        target = new Grid2D<>(true, true);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> target.setOrientation(Mapper2D.NE));
        assertTrue(thrown.getMessage().contains("not supported"), "unexpected exception");
    }

    @Test
    public void testGetWrapped() {
        target = new Grid2D<>(true, true);
        loadGrid();

        checkSize(3, 3);
        for (int dy = 0; dy < 5; dy++) {
            int y0 = dy * testRows2D.length;
            for (int dx = 0; dx < 5; dx++) {
                int x0 = dx * testRows[0].size();
                for (int y = 0; y < testRows2D.length; y++) {
                    int y1 = y + y0;
                    List<GridTestEnum> row = testRows2D[y];
                    for (int x = 0; x < row.size(); x++) {
                        int x1 = x + x0;
                        assertEquals(row.get(x), target.get(x1, y1), "(" + x1 + "," + y1 + ")");
                    }
                }
            }
        }
    }

    @Test
    public void testGetSize() {
        loadGrid();
        // Adjust the internal data to provide unique values for each axis
        getGrid1(target, 0).getPoints().add(GridTestEnum.P);
        checkSize(4, 3);

        Coord2D size = target.getSize();

        assertAll("grid size",
                () -> assertEquals(3, size.getY(), "Y"),
                () -> assertEquals(4, size.getX(), "X")
        );
    }

    @Test
    public void testCompassNeighborStream() {
        loadGrid();

        List<GridTestEnum> neighbors = target.compassNeighborStream(new Coord2D(1, 1))
                .collect(Collectors.toList());

        assertEquals(4, neighbors.size(), "count of neighbors");
        assertAll("neighbors",
                () -> assertTrue(neighbors.contains(GridTestEnum.B), "B"),
                () -> assertTrue(neighbors.contains(GridTestEnum.D), "D"),
                () -> assertTrue(neighbors.contains(GridTestEnum.F), "F"),
                () -> assertTrue(neighbors.contains(GridTestEnum.H), "H")
        );
    }

    @Test
    public void testNeighborStream() {
        loadGrid();

        List<GridTestEnum> neighbors = target.neighborStream(new Coord2D(1, 1))
                .collect(Collectors.toList());

        assertEquals(8, neighbors.size(), "count of neighbors");
        assertAll("neighbors",
                () -> assertTrue(neighbors.contains(GridTestEnum.A), "A"),
                () -> assertTrue(neighbors.contains(GridTestEnum.B), "B"),
                () -> assertTrue(neighbors.contains(GridTestEnum.C), "C"),
                () -> assertTrue(neighbors.contains(GridTestEnum.D), "D"),
                () -> assertTrue(neighbors.contains(GridTestEnum.F), "F"),
                () -> assertTrue(neighbors.contains(GridTestEnum.G), "G"),
                () -> assertTrue(neighbors.contains(GridTestEnum.H), "H"),
                () -> assertTrue(neighbors.contains(GridTestEnum.I), "I")
        );
    }

    @Test
    public void testPointStream() {
        loadGrid();

        List<GridTestEnum> points = target.pointStream()
                .collect(Collectors.toList());

        assertEquals(9, points.size(), "count of points");
        assertAll("points",
                () -> assertTrue(points.contains(GridTestEnum.A), "A"),
                () -> assertTrue(points.contains(GridTestEnum.B), "B"),
                () -> assertTrue(points.contains(GridTestEnum.C), "C"),
                () -> assertTrue(points.contains(GridTestEnum.D), "D"),
                () -> assertTrue(points.contains(GridTestEnum.E), "E"),
                () -> assertTrue(points.contains(GridTestEnum.F), "F"),
                () -> assertTrue(points.contains(GridTestEnum.G), "G"),
                () -> assertTrue(points.contains(GridTestEnum.H), "H"),
                () -> assertTrue(points.contains(GridTestEnum.I), "I")
        );
    }

    @Test
    public void testRowStream() {
        loadGrid();

        List<List<GridTestEnum>> rows = target.rowStream()
                .collect(Collectors.toList());

        assertEquals(3, rows.size(), "count of rows");
        List<GridTestEnum> p0 = rows.get(0);
        assertEquals(3, p0.size(), "count of points in row 0");
        assertAll("points in row 0",
                () -> assertTrue(p0.contains(GridTestEnum.A), "A"),
                () -> assertTrue(p0.contains(GridTestEnum.B), "B"),
                () -> assertTrue(p0.contains(GridTestEnum.C), "C")
        );
        List<GridTestEnum> p1 = rows.get(1);
        assertEquals(3, p1.size(), "count of points in row 1");
        assertAll("points in row 1",
                () -> assertTrue(p1.contains(GridTestEnum.D), "D"),
                () -> assertTrue(p1.contains(GridTestEnum.E), "E"),
                () -> assertTrue(p1.contains(GridTestEnum.F), "F")
        );
        List<GridTestEnum> p2 = rows.get(2);
        assertEquals(3, p2.size(), "count of points in row 2");
        assertAll("points in row 2",
                () -> assertTrue(p2.contains(GridTestEnum.G), "G"),
                () -> assertTrue(p2.contains(GridTestEnum.H), "H"),
                () -> assertTrue(p2.contains(GridTestEnum.I), "I")
        );
    }

    @Test
    public void testDirections() {
        loadGrid();

        List<Coord2D> dirs = target.directions()
                .distinct()
                .collect(Collectors.toList());

        assertEquals(9, dirs.size(), "count of distinct directions to neighbors");
    }
}
