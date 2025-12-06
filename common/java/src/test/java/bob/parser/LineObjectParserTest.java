package bob.parser;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LineObjectParserTest {
    
    private static class Entry {
        public char value;

        public Entry(char value) {
            this.value = value;
        }
    }
    
    private LineObjectParser<Entry> target;
    
    public LineObjectParserTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        target = new LineObjectParser<>(Entry::new);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testEmpty() {
        target.open(0);
        target.close(0);
        
        List<List<Entry>> result = target.getResult();
        assertEquals(0, result.size(), "list size");
    }

    @Test
    public void testSingle() {
        target.open(0);
        target.read(0, "abc");
        target.close(0);
        
        List<List<Entry>> result = target.getResult();
        assertEquals(1, result.size(), "list size");
        List<Entry> entry = result.get(0);
        assertAll("result",
                () -> assertEquals(3, entry.size(), "list size"),
                () -> assertEquals('a', entry.get(0).value, "a"),
                () -> assertEquals('b', entry.get(1).value, "b"),
                () -> assertEquals('c', entry.get(2).value, "c")
        );
    }

    @Test
    public void testMultiline() {
        target.open(0);
        target.read(0, "abc");
        target.read(0, "def");
        target.close(0);
        
        List<List<Entry>> result = target.getResult();
        assertEquals(2, result.size(), "list size");
        List<Entry> e1 = result.get(0);
        assertAll("result",
                () -> assertEquals(3, e1.size(), "list size"),
                () -> assertEquals('a', e1.get(0).value, "a"),
                () -> assertEquals('b', e1.get(1).value, "b"),
                () -> assertEquals('c', e1.get(2).value, "c")
        );
        List<Entry> e2 = result.get(1);
        assertAll("result",
                () -> assertEquals(3, e2.size(), "list size"),
                () -> assertEquals('d', e2.get(0).value, "d"),
                () -> assertEquals('e', e2.get(1).value, "e"),
                () -> assertEquals('f', e2.get(2).value, "f")
        );
    }

    @Test
    public void testMultipart() {
        target.open(0);
        target.read(0, "abc");
        target.close(0);
        target.open(1);
        target.read(1, "def");
        target.close(1);
        
        List<List<Entry>> result = target.getResult();
        assertEquals(2, result.size(), "list size");
        List<Entry> e1 = result.get(0);
        assertAll("result",
                () -> assertEquals(3, e1.size(), "list size"),
                () -> assertEquals('a', e1.get(0).value, "a"),
                () -> assertEquals('b', e1.get(1).value, "b"),
                () -> assertEquals('c', e1.get(2).value, "c")
        );
        List<Entry> e2 = result.get(1);
        assertAll("result",
                () -> assertEquals(3, e2.size(), "list size"),
                () -> assertEquals('d', e2.get(0).value, "d"),
                () -> assertEquals('e', e2.get(1).value, "e"),
                () -> assertEquals('f', e2.get(2).value, "f")
        );
    }
}
