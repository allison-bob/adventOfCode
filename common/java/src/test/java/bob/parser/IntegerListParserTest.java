package bob.parser;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IntegerListParserTest {
    
    public IntegerListParserTest() {
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
        IntegerListParser target = new IntegerListParser();
        
        target.open(0);
        target.read(0, "0,  ,1,2,,3");
        target.read(0, " 4, 5, 6");
        target.read(0, "7,8,9,");
        target.close(0);
        List<Integer> result = target.getResult();
        
        assertEquals(10, result.size(), "number of integers read");
        for (int i = 0; i < result.size(); i++) {
            assertEquals(i, result.get(i), "value at index " + i);
        }
    }
}
