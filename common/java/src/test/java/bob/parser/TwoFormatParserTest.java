package bob.parser;

import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TwoFormatParserTest {

    public TwoFormatParserTest() {
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
        TwoFormatParser<List<Integer>, List<HashSet<String>>> target = new TwoFormatParser<>(new IntegerListParser(),
                new ObjectListParser<>(HashSet::new, HashSet::add));

        target.open(0);
        target.read(0, "1,2,3");
        target.close(0);
        target.open(1);
        target.read(1, "test");
        target.read(1, "this");
        target.read(1, "out");
        target.close(1);
        target.open(2);
        target.read(2, "one");
        target.read(2, "more");
        target.read(2, "test");
        target.close(2);
        TwoFormatParser.Output<List<Integer>, List<HashSet<String>>> result = target.getResult();

        assertEquals(3, result.first.size(), "number of integers read");
        assertEquals(2, result.rest.size(), "number of sets read");
    }
}
