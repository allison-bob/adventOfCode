package bob.parser;

import bob.data.grid.Grid2D;
import bob.data.grid.GridTestEnum;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Grid2DMapParserTest {
    
    public Grid2DMapParserTest() {
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
    public void testResult() {
        Grid2DMapParser<GridTestEnum> target = new Grid2DMapParser<>(false, false, GridTestEnum::valueOf,
                Integer::valueOf);
        
        target.open(0);
        target.read(0, "86");
        target.read(0, "ABC");
        target.read(0, "DEF");
        target.read(0, "GHI");
        target.close(0);
        target.open(1);
        target.read(1, "99");
        target.read(1, "JKL");
        target.read(1, "MNO");
        target.read(1, "PQR");
        target.close(1);
        Map<Integer, Grid2D<GridTestEnum>> result = target.getResult();
        
        assertEquals(2, result.size(), "Number of grids read");
        assertTrue(result.containsKey(86), "Grid for key 86 missing");
        Grid2D g86 = result.get(86);
        assertAll("grid 86 points",
                () -> assertEquals(GridTestEnum.A, g86.get(0, 0), "0,0"),
                () -> assertEquals(GridTestEnum.B, g86.get(1, 0), "1,0"),
                () -> assertEquals(GridTestEnum.C, g86.get(2, 0), "2,0"),
                () -> assertEquals(GridTestEnum.D, g86.get(0, 1), "0,1"),
                () -> assertEquals(GridTestEnum.E, g86.get(1, 1), "1,1"),
                () -> assertEquals(GridTestEnum.F, g86.get(2, 1), "2,1"),
                () -> assertEquals(GridTestEnum.G, g86.get(0, 2), "0,2"),
                () -> assertEquals(GridTestEnum.H, g86.get(1, 2), "1,2"),
                () -> assertEquals(GridTestEnum.I, g86.get(2, 2), "2,2")
        );
        assertTrue(result.containsKey(99), "Grid for key 99 missing");
        Grid2D g99 = result.get(99);
        assertAll("grid 99 points",
                () -> assertEquals(GridTestEnum.J, g99.get(0, 0), "0,0"),
                () -> assertEquals(GridTestEnum.K, g99.get(1, 0), "1,0"),
                () -> assertEquals(GridTestEnum.L, g99.get(2, 0), "2,0"),
                () -> assertEquals(GridTestEnum.M, g99.get(0, 1), "0,1"),
                () -> assertEquals(GridTestEnum.N, g99.get(1, 1), "1,1"),
                () -> assertEquals(GridTestEnum.O, g99.get(2, 1), "2,1"),
                () -> assertEquals(GridTestEnum.P, g99.get(0, 2), "0,2"),
                () -> assertEquals(GridTestEnum.Q, g99.get(1, 2), "1,2"),
                () -> assertEquals(GridTestEnum.R, g99.get(2, 2), "2,2")
        );
    }
}
