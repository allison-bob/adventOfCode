package bob.parser;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SingleLongParserTest {
    
    public SingleLongParserTest() {
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
    public void testEmpty() {
        SingleLongParser target = new SingleLongParser();

        target.open(0);
        target.close(0);
        
        List<Long> entries = target.getResult();
        assertEquals(0, entries.size(), "list size");
    }

    @Test
    public void testNotLong() {
        SingleLongParser target = new SingleLongParser();

        target.open(0);
        Exception e = assertThrows(NumberFormatException.class, () -> target.read(0, "qwerty"));
        
        assertEquals("For input string: \"qwerty\"", e.getMessage(), "exception message");
        List<Long> entries = target.getResult();
        assertEquals(0, entries.size(), "list size");
    }

    @Test
    public void testLong() {
        SingleLongParser target = new SingleLongParser();

        target.open(0);
        target.read(0, "42");
        target.close(0);
        
        List<Long> entries = target.getResult();
        assertAll("result",
                () -> assertEquals(1, entries.size(), "list size"),
                () -> assertEquals(42, entries.get(0), "list content")
        );
    }

    @Test
    public void testLong2() {
        SingleLongParser target = new SingleLongParser();

        target.open(0);
        target.read(0, "42");
        target.read(0, "86");
        target.read(0, "99");
        target.close(0);
        
        List<Long> entries = target.getResult();
        assertAll("result",
                () -> assertEquals(3, entries.size(), "list size"),
                () -> assertEquals(42, entries.get(0), "list content 0"),
                () -> assertEquals(86, entries.get(1), "list content 1"),
                () -> assertEquals(99, entries.get(2), "list content 2")
        );
    }
}
