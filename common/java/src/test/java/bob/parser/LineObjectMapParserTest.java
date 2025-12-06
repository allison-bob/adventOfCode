package bob.parser;

import java.util.Map;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LineObjectMapParserTest {
    
    @Getter
    private class Entry {
        private final String key;
        private final String value;

        public Entry(String line) {
            String[] bits = line.split(",");
            this.key = bits[0];
            this.value = bits[1];
        }
    }
    
    private LineObjectMapParser<String, Entry> target;
    
    public LineObjectMapParserTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        target = new LineObjectMapParser<>(Entry::new, Entry::getKey);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testEmpty() {
        target.open(0);
        target.close(0);
        
        Map<String, Entry> entries = target.getResult();
        assertEquals(0, entries.size(), "list size");
    }

    @Test
    public void testSingle() {
        target.open(0);
        target.read(0, "test,this");
        target.close(0);
        
        Map<String, Entry> entries = target.getResult();
        assertAll("result",
                () -> assertEquals(1, entries.size(), "map size"),
                () -> assertEquals("this", entries.get("test").getValue(), "entry 'test'")
        );
    }

    @Test
    public void testMultiple() {
        target.open(0);
        target.read(0, "test,this");
        target.read(0, "and,that");
        target.read(0, "once,again");
        target.close(0);
        
        Map<String, Entry> entries = target.getResult();
        assertAll("result",
                () -> assertEquals(3, entries.size(), "map size"),
                () -> assertEquals("this", entries.get("test").getValue(), "entry 'test'"),
                () -> assertEquals("that", entries.get("and").getValue(), "entry 'and'"),
                () -> assertEquals("again", entries.get("once").getValue(), "entry 'once'")
        );
    }
}
