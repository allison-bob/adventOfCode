package bob.data.coordinate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Coord1DTest {

    private static final int[] values = new int[]{0, 0, 1, 2};
    private static final Coord1D[] C = new Coord1D[values.length];

    public Coord1DTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        for (int i = 0; i < values.length; i++) {
            C[i] = new Coord1D(values[i]);
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

    @Test
    public void testGetX() {
        assertAll("getX",
                () -> {
                    for (int i = 0; i < values.length; i++) {
                        assertEquals(values[i], C[i].getX(), "C" + i);
                    }
                }
        );
    }

    @Test
    public void testAddOffset() {
        Coord1D result = C[0].addOffset(10);
        assertAll("addOffset",
                () -> assertAll("original unchanged",
                        () -> assertEquals(0, C[0].getX(), "X")
                ),
                () -> assertAll("result",
                        () -> assertEquals(10, result.getX(), "X")
                )
        );
        Coord1D result2 = result.addOffset(C[3]);
        assertAll("addOffset",
                () -> assertAll("original unchanged",
                        () -> assertEquals(10, result.getX(), "X")
                ),
                () -> assertAll("result",
                        () -> assertEquals(12, result2.getX(), "X")
                )
        );
    }

    @Test
    public void testCompareTo() {
        assertAll("compareTo",
                () -> assertEquals(0, C[0].compareTo(C[1]), "C0 <-> C1"),
                () -> assertEquals(-1, C[0].compareTo(C[2]), "C0 <-> C2"),
                () -> assertEquals(-1, C[0].compareTo(C[3]), "C0 <-> C3"),
                () -> assertEquals(1, C[2].compareTo(C[1]), "C2 <-> C1"),
                () -> assertEquals(1, C[3].compareTo(C[1]), "C3 <-> C1")
        );
    }

    @Test
    public void testGetDownDim() {
        assertAll("down dimensions",
                () -> {
                    for (int i = 0; i < values.length; i++) {
                        assertNull(C[i].getDownDim(), "C" + i);
                    }
                }
        );
    }

    @Test
    public void testGetThisDim() {
        assertAll("this dimensions",
                () -> {
                    for (int i = 0; i < values.length; i++) {
                        assertEquals(values[i], C[i].getThisDim(), "C" + i);
                    }
                }
        );
    }

    @Test
    public void testIsNotNegative() {
        assertAll("isNotNegative",
                () -> assertTrue(new Coord1D(1).isNotNegative(), "1"),
                () -> assertTrue(new Coord1D(0).isNotNegative(), "0"),
                () -> assertFalse(new Coord1D(-1).isNotNegative(), "-1")
        );
    }

    @Test
    public void testManhattan() {
        assertAll("manhattan",
                () -> assertEquals(1, new Coord1D(1).manhattan(), "+"),
                () -> assertEquals(1, new Coord1D(-1).manhattan(), "-")
        );
        Coord1D origin = new Coord1D(10);
        assertAll("manhattan",
                () -> assertEquals(9, new Coord1D(1).manhattan(origin), "+"),
                () -> assertEquals(11, new Coord1D(-1).manhattan(origin), "-")
        );
    }

    @Test
    public void testValues() {
        assertAll("values",
                () -> {
                    for (int i = 0; i < values.length; i++) {
                        assertEquals("" + values[i], C[i].values(), "C" + i);
                    }
                }
        );
    }

    @Test
    public void testHashCode() {
        assertAll("hashCode",
                () -> assertEquals(C[0].hashCode(), C[1].hashCode(), "C0 <-> C1"),
                () -> assertNotEquals(C[0].hashCode(), C[2].hashCode(), "C0 <-> C2"),
                () -> assertNotEquals(C[0].hashCode(), C[3].hashCode(), "C0 <-> C3")
        );
    }

    @Test
    public void testEquals() {
        assertAll("equals",
                () -> assertTrue(C[0].equals(C[1]), "C0 <-> C1"),
                () -> assertFalse(C[0].equals(C[2]), "C0 <-> C2"),
                () -> assertFalse(C[0].equals(C[3]), "C0 <-> C3")
        );
    }

    @Test
    public void testToString() {
        assertAll("toString",
                () -> {
                    for (int i = 0; i < values.length; i++) {
                        assertEquals("(" + values[i] + ")", C[i].toString(), "C" + i);
                    }
                }
        );
    }
}
