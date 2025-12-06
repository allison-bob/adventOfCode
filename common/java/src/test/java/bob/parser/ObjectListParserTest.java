package bob.parser;

import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ObjectListParserTest {

    public ObjectListParserTest() {
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
        ObjectListParser<HashSet<String>> target = new ObjectListParser<>(HashSet::new, HashSet::add);

        target.open(0);
        target.read(0, "test");
        target.read(0, "this");
        target.read(0, "out");
        target.close(0);
        target.open(1);
        target.read(1, "one");
        target.read(1, "more");
        target.read(1, "test");
        target.close(1);
        List<HashSet<String>> result = target.getResult();

        assertEquals(2, result.size(), "Number of objects read");
        HashSet<String> s0 = result.get(0);
        assertAll("first set contents",
                () -> assertTrue(s0.contains("test"), "test"),
                () -> assertTrue(s0.contains("this"), "this"),
                () -> assertTrue(s0.contains("out"), "out")
        );
        HashSet<String> s1 = result.get(1);
        assertAll("second set contents",
                () -> assertTrue(s1.contains("one"), "one"),
                () -> assertTrue(s1.contains("more"), "more"),
                () -> assertTrue(s1.contains("test"), "test")
        );
    }
}
