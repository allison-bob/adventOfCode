package bob.parser;

import bob.data.grid.Grid2D;
import bob.data.grid.GridTestEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Grid2DParserTest {

    public Grid2DParserTest() {
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
        Grid2DParser<GridTestEnum> target = new Grid2DParser<>(false, false, GridTestEnum::valueOf);

        target.open(0);
        target.read(0, "ABC");
        target.read(0, "DEF");
        target.read(0, "GHI");
        target.close(0);
        Grid2D<GridTestEnum> result = target.getResult();

        assertAll("grid points",
                () -> assertEquals(GridTestEnum.A, result.get(0, 0), "0,0"),
                () -> assertEquals(GridTestEnum.B, result.get(1, 0), "1,0"),
                () -> assertEquals(GridTestEnum.C, result.get(2, 0), "2,0"),
                () -> assertEquals(GridTestEnum.D, result.get(0, 1), "0,1"),
                () -> assertEquals(GridTestEnum.E, result.get(1, 1), "1,1"),
                () -> assertEquals(GridTestEnum.F, result.get(2, 1), "2,1"),
                () -> assertEquals(GridTestEnum.G, result.get(0, 2), "0,2"),
                () -> assertEquals(GridTestEnum.H, result.get(1, 2), "1,2"),
                () -> assertEquals(GridTestEnum.I, result.get(2, 2), "2,2")
        );
    }
}
