package bob.parser;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PartObjectMapParserTest {
    
    private PartObjectMapParser<String, String> target;
    
    public PartObjectMapParserTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        target = new PartObjectMapParser<>(line -> line, line -> line);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testEmpty() {
        target.open(0);
        target.close(0);
        
        Map<String, List<String>> entries = target.getResult();
        assertEquals(0, entries.size(), "map size");
    }

    @Test
    public void testSingle() {
        target.open(0);
        target.read(0, "h1");
        target.read(0, "test");
        target.read(0, "this");
        target.close(0);
        
        Map<String, List<String>> entries = target.getResult();
        assertEquals(1, entries.size(), "map size");
        assertTrue(entries.containsKey("h1"), "map contains key h1");
        List<String> list = entries.get("h1");
        assertAll("h1 list",
                () -> assertEquals(2, list.size(), "list size"),
                () -> assertEquals("test", list.get(0), "entry 0"),
                () -> assertEquals("this", list.get(1), "entry 1")
        );
    }

    @Test
    public void testMultiple() {
        target.open(0);
        target.read(0, "h1");
        target.read(0, "test");
        target.read(0, "this");
        target.close(0);
        target.open(1);
        target.read(1, "h2");
        target.read(1, "and");
        target.read(1, "that");
        target.close(1);
        target.open(2);
        target.read(2, "h3");
        target.read(2, "once");
        target.read(2, "again");
        target.close(2);
        
        Map<String, List<String>> entries = target.getResult();
        assertEquals(3, entries.size(), "map size");
        assertTrue(entries.containsKey("h1"), "map contains key h1");
        List<String> list1 = entries.get("h1");
        assertAll("h1 list",
                () -> assertEquals(2, list1.size(), "list size"),
                () -> assertEquals("test", list1.get(0), "entry 0"),
                () -> assertEquals("this", list1.get(1), "entry 1")
        );
        assertTrue(entries.containsKey("h2"), "map contains key h2");
        List<String> list2 = entries.get("h2");
        assertAll("h2 list",
                () -> assertEquals(2, list2.size(), "list size"),
                () -> assertEquals("and", list2.get(0), "entry 0"),
                () -> assertEquals("that", list2.get(1), "entry 1")
        );
        assertTrue(entries.containsKey("h3"), "map contains key h3");
        List<String> list3 = entries.get("h3");
        assertAll("h3 list",
                () -> assertEquals(2, list3.size(), "list size"),
                () -> assertEquals("once", list3.get(0), "entry 0"),
                () -> assertEquals("again", list3.get(1), "entry 1")
        );
    }
}
