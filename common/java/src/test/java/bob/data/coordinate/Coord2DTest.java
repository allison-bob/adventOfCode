package bob.data.coordinate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Coord2DTest {

    private static final int[] values = new int[]{0, 0, 1, 2};
    private static final Coord1D[] D = new Coord1D[values.length];
    private static final Coord2D[] C = new Coord2D[values.length];
    
    public Coord2DTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        for (int i = 0; i < values.length; i++) {
            D[i] = new Coord1D(3);
            C[i] = new Coord2D(3, values[i]);
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
                        assertEquals(3, C[i].getX(), "C" + i);
                    }
                }
        );
    }

    @Test
    public void testGetY() {
        assertAll("getY",
                () -> {
                    for (int i = 0; i < values.length; i++) {
                        assertEquals(values[i], C[i].getY(), "C" + i);
                    }
                }
        );
    }

    @Test
    public void testAddOffset() {
        Coord2D result = C[0].addOffset(10);
        assertAll("addOffset",
                () -> assertAll("original unchanged",
                        () -> assertEquals(3, C[0].getX(), "X"),
                        () -> assertEquals(0, C[0].getY(), "Y")
                ),
                () -> assertAll("result",
                        () -> assertEquals(13, result.getX(), "X"),
                        () -> assertEquals(10, result.getY(), "Y")
                )
        );
        Coord2D result2 = result.addOffset(C[3]);
        assertAll("addOffset",
                () -> assertAll("original unchanged",
                        () -> assertEquals(13, result.getX(), "X"),
                        () -> assertEquals(10, result.getY(), "Y")
                ),
                () -> assertAll("result",
                        () -> assertEquals(16, result2.getX(), "X"),
                        () -> assertEquals(12, result2.getY(), "Y")
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
                        assertEquals(D[i], C[i].getDownDim(), "C" + i);
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
                () -> assertTrue(new Coord2D(1, 1).isNotNegative(), "1,1"),
                () -> assertTrue(new Coord2D(1, 0).isNotNegative(), "1,0"),
                () -> assertFalse(new Coord2D(1, -1).isNotNegative(), "1,-1"),
                () -> assertTrue(new Coord2D(0, 1).isNotNegative(), "0,1"),
                () -> assertFalse(new Coord2D(-1, 1).isNotNegative(), "-1,1")
        );
    }

    @Test
    public void testManhattan() {
        assertAll("manhattan",
                () -> assertEquals(3, new Coord2D(1, 2).manhattan(), "+,+"),
                () -> assertEquals(3, new Coord2D(-1, 2).manhattan(), "-,+"),
                () -> assertEquals(3, new Coord2D(1, -2).manhattan(), "+,-")
        );
        Coord2D origin = new Coord2D(10, 10);
        assertAll("manhattan",
                () -> assertEquals(17, new Coord2D(1, 2).manhattan(origin), "+,+"),
                () -> assertEquals(19, new Coord2D(-1, 2).manhattan(origin), "-,+"),
                () -> assertEquals(21, new Coord2D(1, -2).manhattan(origin), "+,-")
        );
    }

    @Test
    public void testValues() {
        assertAll("values",
                () -> {
                    for (int i = 0; i < values.length; i++) {
                        assertEquals("3," + values[i], C[i].values(), "C" + i);
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
                        assertEquals("(3," + values[i] + ")", C[i].toString(), "C" + i);
                    }
                }
        );
    }
}
