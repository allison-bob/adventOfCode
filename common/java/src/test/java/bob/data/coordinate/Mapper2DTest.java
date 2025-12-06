package bob.data.coordinate;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

public class Mapper2DTest {

    // Build the test coordinates
    //  . . | A .
    //  . . B . C
    //  - - + D -
    //  . . | . .
    //  . . | . .
    private static final Coord2D CA = new Coord2D(1, 2);
    private static final Coord2D CB = new Coord2D(0, 1);
    private static final Coord2D CC = new Coord2D(2, 1);
    private static final Coord2D CD = new Coord2D(1, 0);
    
    public Mapper2DTest() {
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
    public void testMap() {
        assertAll("mapping",
                () -> assertAll("EN",
                        //  . . | A .
                        //  . . B . C
                        //  - - + D -
                        //  . . | . .
                        //  . . | . .
                        () -> assertEquals(new Coord2D(1, 2), Mapper2D.EN.map(CA), "A"),
                        () -> assertEquals(new Coord2D(0, 1), Mapper2D.EN.map(CB), "B"),
                        () -> assertEquals(new Coord2D(2, 1), Mapper2D.EN.map(CC), "C"),
                        () -> assertEquals(new Coord2D(1, 0), Mapper2D.EN.map(CD), "D")
                ),
                () -> assertAll("ES",
                        //  . . | . .
                        //  . . | . .
                        //  - - + D -
                        //  . . B . C
                        //  . . | A .
                        () -> assertEquals(new Coord2D(1, -2), Mapper2D.ES.map(CA), "A"),
                        () -> assertEquals(new Coord2D(0, -1), Mapper2D.ES.map(CB), "B"),
                        () -> assertEquals(new Coord2D(2, -1), Mapper2D.ES.map(CC), "C"),
                        () -> assertEquals(new Coord2D(1, 0), Mapper2D.ES.map(CD), "D")
                ),
                () -> assertAll("NE",
                        //  . . | C .
                        //  . . D . A
                        //  - - + B -
                        //  . . | . .
                        //  . . | . .
                        () -> assertEquals(new Coord2D(2, 1), Mapper2D.NE.map(CA), "A"),
                        () -> assertEquals(new Coord2D(1, 0), Mapper2D.NE.map(CB), "B"),
                        () -> assertEquals(new Coord2D(1, 2), Mapper2D.NE.map(CC), "C"),
                        () -> assertEquals(new Coord2D(0, 1), Mapper2D.NE.map(CD), "D")
                ),
                () -> assertAll("NW",
                        //  . C | . .
                        //  A . D . .
                        //  - B + - -
                        //  . . | . .
                        //  . . | . .
                        () -> assertEquals(new Coord2D(-2, 1), Mapper2D.NW.map(CA), "A"),
                        () -> assertEquals(new Coord2D(-1, 0), Mapper2D.NW.map(CB), "B"),
                        () -> assertEquals(new Coord2D(-1, 2), Mapper2D.NW.map(CC), "C"),
                        () -> assertEquals(new Coord2D(0, 1), Mapper2D.NW.map(CD), "D")
                ),
                () -> assertAll("SE",
                        //  . . | . .
                        //  . . | . .
                        //  - - + B -
                        //  . . D . A
                        //  . . | C .
                        () -> assertEquals(new Coord2D(2, -1), Mapper2D.SE.map(CA), "A"),
                        () -> assertEquals(new Coord2D(1, 0), Mapper2D.SE.map(CB), "B"),
                        () -> assertEquals(new Coord2D(1, -2), Mapper2D.SE.map(CC), "C"),
                        () -> assertEquals(new Coord2D(0, -1), Mapper2D.SE.map(CD), "D")
                ),
                () -> assertAll("SW",
                        //  . . | . .
                        //  . . | . .
                        //  - B + - -
                        //  A . D . .
                        //  . C | . .
                        () -> assertEquals(new Coord2D(-2, -1), Mapper2D.SW.map(CA), "A"),
                        () -> assertEquals(new Coord2D(-1, 0), Mapper2D.SW.map(CB), "B"),
                        () -> assertEquals(new Coord2D(-1, -2), Mapper2D.SW.map(CC), "C"),
                        () -> assertEquals(new Coord2D(0, -1), Mapper2D.SW.map(CD), "D")
                ),
                () -> assertAll("WN",
                        //  . A | . .
                        //  C . B . .
                        //  - D + - -
                        //  . . | . .
                        //  . . | . .
                        () -> assertEquals(new Coord2D(-1, 2), Mapper2D.WN.map(CA), "A"),
                        () -> assertEquals(new Coord2D(0, 1), Mapper2D.WN.map(CB), "B"),
                        () -> assertEquals(new Coord2D(-2, 1), Mapper2D.WN.map(CC), "C"),
                        () -> assertEquals(new Coord2D(-1, 0), Mapper2D.WN.map(CD), "D")
                ),
                () -> assertAll("WS",
                        //  . . | . .
                        //  . . | . .
                        //  - D + - -
                        //  C . B . .
                        //  . A | . .
                        () -> assertEquals(new Coord2D(-1, -2), Mapper2D.WS.map(CA), "A"),
                        () -> assertEquals(new Coord2D(0, -1), Mapper2D.WS.map(CB), "B"),
                        () -> assertEquals(new Coord2D(-2, -1), Mapper2D.WS.map(CC), "C"),
                        () -> assertEquals(new Coord2D(-1, 0), Mapper2D.WS.map(CD), "D")
                )
        );
    }

    @Test
    public void testInverses() {
        List<Executable> tests = new ArrayList<>();
        for (Mapper2D m : Mapper2D.values()) {
            tests.add(() -> testOneInverse(m));
        }
        assertAll("inverses", tests.stream());
    }

    private void testOneInverse(Mapper2D m) {
        Mapper2D i = m.inverse();
        for (Mapper2D u : Mapper2D.values()) {
            if (CA.equals(u.map(m.map(CA)))) {
                assertEquals(i, u, m.name());
            }
        }
    }
}
