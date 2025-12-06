package bob.data.grid;

import bob.data.coordinate.Coord3D;
import bob.data.coordinate.Mapper3D;
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

public class Grid3DTest extends GridTestBase {

    private final Grid3D<GridTestEnum> target = new Grid3D<>();

    public Grid3DTest() {
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
        target.getSubgrids().clear();
        for (List<GridTestEnum>[] rows : testRows3D) {
            target.getSubgrids().add(loadGrid2(rows));
        }
    }

    // This method is used in many tests to validate that the grid dimensions are correct before
    // using loops to verify content at various coordinates
    private void checkSize(int x, int y, int z) {
        assertAll("grid size",
                () -> assertEquals(z, getGrid3Size(target), "Z"),
                () -> assertEquals(y, getGrid2Size(getGrid2(target, 0)), "Y"),
                () -> assertEquals(x, getGrid1Size(getGrid1(getGrid2(target, 0), 0)), "X")
        );
    }

    @Test
    public void testAddBorder() {
        loadGrid();

        target.addBorder(GridTestEnum.BORDER);

        checkSize(5, 5, 5);
        // Borders added to existing subgrids is tested in the down-dimension test, just need to verify here
        // that the new subgrids were added correctly
        Grid2D<GridTestEnum> bgz0 = getGrid2(target, 0);
        Grid2D<GridTestEnum> bgz4 = getGrid2(target, 4);
        for (int y = 0; y < getGrid2Size(bgz0); y++) {
            Grid1D<GridTestEnum> bgy0 = getGrid1(bgz0, y);
            Grid1D<GridTestEnum> bgy4 = getGrid1(bgz4, y);
            for (int x = 0; x < getGrid1Size(bgy0); x++) {
                assertEquals(GridTestEnum.BORDER, getGrid0(bgy0, x), "(" + x + "," + y + ",0)");
                assertEquals(GridTestEnum.BORDER, getGrid0(bgy4, x), "(" + x + "," + y + ",4)");
            }
        }
    }

    @Test
    public void testAddRowEmpty() {
        target.addRow(testRows[0]);

        checkSize(3, 1, 1);
        Grid1D<GridTestEnum> g0 = getGrid1(getGrid2(target, 0), 0);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(g0, x), "(" + x + ",0,0)");
        }
    }

    @Test
    public void testAddRowNotEmpty() {
        target.addRow(testRows[0]);
        target.addRow(testRows[1]);

        checkSize(3, 2, 1);
        Grid1D<GridTestEnum> g0 = getGrid1(getGrid2(target, 0), 0);
        Grid1D<GridTestEnum> g1 = getGrid1(getGrid2(target, 0), 1);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(g0, x), "(" + x + ",0,0)");
            assertEquals(testRows[1].get(x), getGrid0(g1, x), "(" + x + ",1,0)");
        }
    }

    @Test
    public void testAddRowLNegative() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> target.addRow(new Coord3D(0, 0, -3), testRows[0]));
        assertTrue(thrown.getMessage().contains("Negative"), "Unexpected exception message");
    }

    @Test
    public void testAddRowLEmptyBadCoord() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> target.addRow(new Coord3D(0, 0, 1), testRows[0]));
        assertTrue(thrown.getMessage().contains("first row"), "Unexpected exception message");
    }

    @Test
    public void testAddRowLNotEmptyBadCoord() {
        target.addRow(new Coord3D(0, 0, 0), testRows[0]);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> target.addRow(new Coord3D(0, 0, 2), testRows[0]));
        assertTrue(thrown.getMessage().contains("too large"), "Unexpected exception message");
    }

    @Test
    public void testAddRowLEmpty() {
        target.addRow(new Coord3D(0, 0, 0), testRows[0]);

        checkSize(3, 1, 1);
        Grid1D<GridTestEnum> g0 = getGrid1(getGrid2(target, 0), 0);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(g0, x), "(" + x + ",0,0)");
        }
    }

    @Test
    public void testAddRowLNotEmpty() {
        target.addRow(new Coord3D(0, 0, 0), testRows[0]);
        target.addRow(new Coord3D(0, 0, 1), testRows[1]);

        checkSize(3, 1, 2);
        Grid1D<GridTestEnum> g0 = getGrid1(getGrid2(target, 0), 0);
        Grid1D<GridTestEnum> g1 = getGrid1(getGrid2(target, 1), 0);
        for (int x = 0; x < testRows[0].size(); x++) {
            assertEquals(testRows[0].get(x), getGrid0(g0, x), "(" + x + ",0,0)");
            assertEquals(testRows[1].get(x), getGrid0(g1, x), "(" + x + ",0,1)");
        }
    }

    @Test
    public void testDump() {
        loadGrid();
        String expected = """
                          z = 0
                          ABC
                          DEF
                          GHI
                          
                          z = 1
                          JKL
                          MNO
                          PQR
                          
                          z = 2
                          STU
                          VWX
                          YZAA
                          
                          """;

        String actual = target.dump(GridTestEnum::toString);

        assertEquals(expected, actual, "dump output");
    }

    @ParameterizedTest
    @EnumSource
    public void testMappingForNull(Mapper3D orient) {
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

        checkSize(3, 3, 3);
        for (int z = 0; z < testRows3D.length; z++) {
            List<GridTestEnum>[] planes = testRows3D[z];
            for (int y = 0; y < planes.length; y++) {
                List<GridTestEnum> row = planes[y];
                for (int x = 0; x < row.size(); x++) {
                    assertEquals(row.get(x), target.get(x, y, z), "(" + x + "," + y + "," + z + ")");
                }
            }
        }
    }

    @Test
    public void testGetOutside() {
        loadGrid();

        checkSize(3, 3, 3);
        assertAll("low coordinate",
                () -> assertNull(target.get(-1, 0, 0), "X"),
                () -> assertNull(target.get(0, -1, 0), "Y"),
                () -> assertNull(target.get(0, 0, -1), "Z")
        );
        assertAll("high coordinate",
                () -> assertNull(target.get(3, 0, 0), "X"),
                () -> assertNull(target.get(0, 3, 0), "Y"),
                () -> assertNull(target.get(0, 0, 3), "Z")
        );
    }

    @Test
    public void testGetMapped() {
        loadGrid();
        target.setOrientation(Mapper3D.DWS);

        checkSize(3, 3, 3);
        for (int z = 0; z < testRows3D.length; z++) {
            List<GridTestEnum>[] planes = testRows3D[z];
            for (int y = 0; y < planes.length; y++) {
                List<GridTestEnum> row = planes[y];
                for (int x = 0; x < row.size(); x++) {
                    assertEquals(row.get(x), target.get(z, x, y), "(" + z + "," + x + "," + y + ")");
                }
            }
        }
    }

    @Test
    public void testGetSize() {
        loadGrid();
        // Adjust the internal data to provide unique values for each axis
        target.getSubgrids().remove(2);
        getGrid1(getGrid2(target, 0), 0).getPoints().add(GridTestEnum.P);
        checkSize(4, 3, 2);

        Coord3D size = target.getSize();

        assertAll("grid size",
                () -> assertEquals(2, size.getZ(), "Z"),
                () -> assertEquals(3, size.getY(), "Y"),
                () -> assertEquals(4, size.getX(), "X")
        );
    }

    @Test
    public void testNeighborStream() {
        loadGrid();

        List<GridTestEnum> neighbors = target.neighborStream(new Coord3D(1, 1, 1))
                .collect(Collectors.toList());
        assertEquals(26, neighbors.size(), "count of neighbors");
        for (GridTestEnum e : GridTestEnum.values()) {
            switch (e) {
                case BORDER, N -> assertFalse(neighbors.contains(e), e + " is a neighbor");
                default -> assertTrue(neighbors.contains(e), e + " not a neighbor");
            }
        }
    }

    @Test
    public void testPointStream() {
        loadGrid();

        List<GridTestEnum> points = target.pointStream()
                .collect(Collectors.toList());

        assertEquals(27, points.size(), "count of points");
        for (GridTestEnum e : GridTestEnum.values()) {
            switch (e) {
                case BORDER -> assertFalse(points.contains(e), e + " is a point");
                default -> assertTrue(points.contains(e), e + " not a point");
            }
        }
    }

    @Test
    public void testRowStream() {
        loadGrid();

        List<List<GridTestEnum>> rows = target.rowStream()
                .collect(Collectors.toList());

        assertEquals(9, rows.size(), "count of rows");
        GridTestEnum[] ea = GridTestEnum.values();
        for (int i = 0; i < rows.size(); i++) {
            List<GridTestEnum> points = rows.get(i);
            int j = i;
            assertEquals(3, points.size(), "count of points in row " + i);
            assertAll("points in row " + i,
                    () -> assertTrue(points.contains(ea[(3 * j) + 0]), ea[(3 * j) + 0].name()),
                    () -> assertTrue(points.contains(ea[(3 * j) + 1]), ea[(3 * j) + 1].name()),
                    () -> assertTrue(points.contains(ea[(3 * j) + 2]), ea[(3 * j) + 2].name())
            );
        }
    }

    @Test
    public void testDirections() {
        loadGrid();

        List<Coord3D> dirs = target.directions()
                .distinct()
                .collect(Collectors.toList());

        assertEquals(27, dirs.size(), "count of distinct directions to neighbors");
    }
}
