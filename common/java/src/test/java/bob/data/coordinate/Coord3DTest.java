package bob.data.coordinate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Coord3DTest {

    private static final int[] values = new int[]{0, 0, 1, 2};
    private static final Coord2D[] D = new Coord2D[values.length];
    private static final Coord3D[] C = new Coord3D[values.length];
    
    public Coord3DTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
        for (int i = 0; i < values.length; i++) {
            D[i] = new Coord2D(3, 4);
            C[i] = new Coord3D(3, 4, values[i]);
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
                        assertEquals(4, C[i].getY(), "C" + i);
                    }
                }
        );
    }

    @Test
    public void testGetZ() {
        assertAll("getZ",
                () -> {
                    for (int i = 0; i < values.length; i++) {
                        assertEquals(values[i], C[i].getZ(), "C" + i);
                    }
                }
        );
    }

    @Test
    public void testAddOffset() {
        Coord3D result = C[0].addOffset(10);
        assertAll("addOffset",
                () -> assertAll("original unchanged",
                        () -> assertEquals(3, C[0].getX(), "X"),
                        () -> assertEquals(4, C[0].getY(), "Y"),
                        () -> assertEquals(0, C[0].getZ(), "Z")
                ),
                () -> assertAll("result",
                        () -> assertEquals(13, result.getX(), "X"),
                        () -> assertEquals(14, result.getY(), "Y"),
                        () -> assertEquals(10, result.getZ(), "Z")
                )
        );
        Coord3D result2 = result.addOffset(C[3]);
        assertAll("addOffset2",
                () -> assertAll("original unchanged",
                        () -> assertEquals(13, result.getX(), "X"),
                        () -> assertEquals(14, result.getY(), "Y"),
                        () -> assertEquals(10, result.getZ(), "Z")
                ),
                () -> assertAll("result",
                        () -> assertEquals(16, result2.getX(), "X"),
                        () -> assertEquals(18, result2.getY(), "Y"),
                        () -> assertEquals(12, result2.getZ(), "Z")
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
                () -> assertTrue(new Coord3D(1, 1, 1).isNotNegative(), "1,1,1"),
                () -> assertTrue(new Coord3D(1, 1, 0).isNotNegative(), "1,1,0"),
                () -> assertFalse(new Coord3D(1, 1, -1).isNotNegative(), "1,1,-1"),
                () -> assertTrue(new Coord3D(1, 0, 1).isNotNegative(), "1,0,1"),
                () -> assertFalse(new Coord3D(1, -1, 1).isNotNegative(), "1,-1,1"),
                () -> assertTrue(new Coord3D(0, 1, 1).isNotNegative(), "0,1,1"),
                () -> assertFalse(new Coord3D(-1, 1, 1).isNotNegative(), "-1,1,1")
        );
    }

    @Test
    public void testManhattan() {
        assertAll("manhattan",
                () -> assertEquals(6, new Coord3D(1, 2, 3).manhattan(), "+,+,+"),
                () -> assertEquals(6, new Coord3D(-1, 2, 3).manhattan(), "-,+,+"),
                () -> assertEquals(6, new Coord3D(1, -2, 3).manhattan(), "+,-,+"),
                () -> assertEquals(6, new Coord3D(1, 2, -3).manhattan(), "+,+,-")
        );
        Coord3D origin = new Coord3D(10, 10, 10);
        assertAll("manhattan",
                () -> assertEquals(24, new Coord3D(1, 2, 3).manhattan(origin), "+,+,+"),
                () -> assertEquals(26, new Coord3D(-1, 2, 3).manhattan(origin), "-,+,+"),
                () -> assertEquals(28, new Coord3D(1, -2, 3).manhattan(origin), "+,-,+"),
                () -> assertEquals(30, new Coord3D(1, 2, -3).manhattan(origin), "+,+,-")
        );
    }

    @Test
    public void testValues() {
        assertAll("values",
                () -> {
                    for (int i = 0; i < values.length; i++) {
                        assertEquals("3,4," + values[i], C[i].values(), "C" + i);
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
                        assertEquals("(3,4," + values[i] + ")", C[i].toString(), "C" + i);
                    }
                }
        );
    }
}
