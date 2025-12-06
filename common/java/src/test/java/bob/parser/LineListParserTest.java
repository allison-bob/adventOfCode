package bob.parser;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LineListParserTest {
    
    private LineListParser<Long> target;
    
    public LineListParserTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        target = new LineListParser<>(":", Long::valueOf);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testEmpty() {
        target.open(0);
        target.close(0);
        
        List<List<Long>> result = target.getResult();
        assertEquals(0, result.size(), "list size");
    }

    @Test
    public void testSingle() {
        target.open(0);
        target.read(0, "1:2::3");
        target.close(0);
        
        List<List<Long>> result = target.getResult();
        assertEquals(1, result.size(), "list size");
        List<Long> entry = result.get(0);
        assertAll("result",
                () -> assertEquals(3, entry.size(), "row 1 list size"),
                () -> assertEquals(1L, entry.get(0), "row 1, column 1"),
                () -> assertEquals(2L, entry.get(1), "row 1, column 2"),
                () -> assertEquals(3L, entry.get(2), "row 1, column 3")
        );
    }

    @Test
    public void testMultiline() {
        target.open(0);
        target.read(0, ":25::50:::::::75");
        target.read(0, "125:150:175");
        target.close(0);
        
        List<List<Long>> result = target.getResult();
        assertEquals(2, result.size(), "list size");
        List<Long> e1 = result.get(0);
        assertAll("result",
                () -> assertEquals(3, e1.size(), "row 1 list size"),
                () -> assertEquals(25L, e1.get(0), "row 1, column 1"),
                () -> assertEquals(50L, e1.get(1), "row 1, column 2"),
                () -> assertEquals(75L, e1.get(2), "row 1, column 3")
        );
        List<Long> e2 = result.get(1);
        assertAll("result",
                () -> assertEquals(3, e2.size(), "row 2 list size"),
                () -> assertEquals(125L, e2.get(0), "row 2, column 1"),
                () -> assertEquals(150L, e2.get(1), "row 2, column 2"),
                () -> assertEquals(175L, e2.get(2), "row 2, column 3")
        );
    }
}
