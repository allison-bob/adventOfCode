package bob.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RangeTest {
    
    public RangeTest() {
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
    public void testParse1NoDelim() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("1:2"));
        assertTrue(thrown.getMessage().contains("Missing"), "Unexpected exception message");
    }

    @Test
    public void testParse1DelimStart() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("-99"));
        assertTrue(thrown.getMessage().contains("Blank lower"), "Unexpected exception message");
    }

    @Test
    public void testParse1DelimEnd1() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("1-"));
        assertTrue(thrown.getMessage().contains("Invalid"), "Unexpected exception message");
    }

    @Test
    public void testParse1DelimEnd2() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("1- "));
        assertTrue(thrown.getMessage().contains("Blank upper"), "Unexpected exception message");
    }

    @Test
    public void testParse1BadLower() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("i-9"));
        assertSame(NumberFormatException.class, thrown.getClass(), "Unexpected class thrown");
    }

    @Test
    public void testParse1BadUpper() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("1-1o"));
        assertSame(NumberFormatException.class, thrown.getClass(), "Unexpected class thrown");
    }

    @Test
    public void testParse1Good() {
        Range result = Range.parse("1-10");
        assertAll("parsing 1-10",
                () -> assertEquals(result.getLow(), 1, "lower bound"),
                () -> assertEquals(result.getHigh(), 10, "upper bound"),
                () -> assertEquals(result.toString(), "[1..10]", "toString")
        );
    }

    @Test
    public void testParse1Inverted() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("10-1"));
        assertTrue(thrown.getMessage().contains("Inverted"), "Unexpected exception message");
    }

    @Test
    public void testParse2NoDelim() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("1:2", "/"));
        assertTrue(thrown.getMessage().contains("Missing"), "Unexpected exception message");
    }

    @Test
    public void testParse2DelimStart() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("/99", "/"));
        assertTrue(thrown.getMessage().contains("Blank lower"), "Unexpected exception message");
    }

    @Test
    public void testParse2DelimEnd1() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("1/", "/"));
        assertTrue(thrown.getMessage().contains("Invalid"), "Unexpected exception message");
    }

    @Test
    public void testParse2DelimEnd2() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("1/ ", "/"));
        assertTrue(thrown.getMessage().contains("Blank upper"), "Unexpected exception message");
    }

    @Test
    public void testParse2BadLower() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("i/9", "/"));
        assertSame(NumberFormatException.class, thrown.getClass(), "Unexpected class thrown");
    }

    @Test
    public void testParse2BadUpper() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("1/1o", "/"));
        assertSame(NumberFormatException.class, thrown.getClass(), "Unexpected class thrown");
    }

    @Test
    public void testParse2Good() {
        Range result = Range.parse("1/10", "/");
        assertAll("parsing 1/10",
                () -> assertEquals(result.getLow(), 1, "lower bound"),
                () -> assertEquals(result.getHigh(), 10, "upper bound"),
                () -> assertEquals(result.toString(), "[1..10]", "toString")
        );
    }

    @Test
    public void testParse2Inverted() {
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> Range.parse("10/1", "/"));
        assertTrue(thrown.getMessage().contains("Inverted"), "Unexpected exception message");
    }

    @Test
    public void testOverlapsFullyBelow() {
        Range r1 = new Range(100, 199);
        Range r2 = new Range(200, 299);
        assertFalse(r2.overlaps(r1), "Unexpected overlap indication");
    }

    @Test
    public void testOverlapsFullyAbove() {
        Range r1 = new Range(100, 199);
        Range r2 = new Range(200, 299);
        assertFalse(r1.overlaps(r2), "Unexpected overlap indication");
    }

    @Test
    public void testOverlapsTouchesBelow() {
        Range r1 = new Range(100, 200);
        Range r2 = new Range(200, 299);
        assertTrue(r2.overlaps(r1), "Unexpected overlap indication");
    }

    @Test
    public void testOverlapsTouchesAbove() {
        Range r1 = new Range(100, 200);
        Range r2 = new Range(200, 299);
        assertTrue(r1.overlaps(r2), "Unexpected overlap indication");
    }

    @Test
    public void testOverlapsBelow() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(200, 299);
        assertTrue(r2.overlaps(r1), "Unexpected overlap indication");
    }

    @Test
    public void testOverlapsAbove() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(200, 299);
        assertTrue(r1.overlaps(r2), "Unexpected overlap indication");
    }

    @Test
    public void testOverlapsSubset() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(150, 200);
        assertTrue(r1.overlaps(r2), "Unexpected overlap indication");
    }

    @Test
    public void testOverlapsSuperset() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(150, 200);
        assertTrue(r2.overlaps(r1), "Unexpected overlap indication");
    }

    @Test
    public void testMergeWithTouchesBelow() {
        Range r1 = new Range(100, 200);
        Range r2 = new Range(200, 299);
        Range result = r2.mergeWith(r1);
        assertAll("merge " + r2 + " with " + r1,
                () -> assertEquals(result.getLow(), 100, "lower bound"),
                () -> assertEquals(result.getHigh(), 299, "upper bound")
        );
    }

    @Test
    public void testMergeWithTouchesAbove() {
        Range r1 = new Range(100, 200);
        Range r2 = new Range(200, 299);
        Range result = r1.mergeWith(r2);
        assertAll("merge " + r1 + " with " + r2,
                () -> assertEquals(result.getLow(), 100, "lower bound"),
                () -> assertEquals(result.getHigh(), 299, "upper bound")
        );
    }

    @Test
    public void testMergeWithOverlapsBelow() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(200, 299);
        Range result = r2.mergeWith(r1);
        assertAll("merge " + r2 + " with " + r1,
                () -> assertEquals(result.getLow(), 100, "lower bound"),
                () -> assertEquals(result.getHigh(), 299, "upper bound")
        );
    }

    @Test
    public void testMergeWithOverlapsAbove() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(200, 299);
        Range result = r1.mergeWith(r2);
        assertAll("merge " + r1 + " with " + r2,
                () -> assertEquals(result.getLow(), 100, "lower bound"),
                () -> assertEquals(result.getHigh(), 299, "upper bound")
        );
    }

    @Test
    public void testMergeWithSubset() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(150, 200);
        Range result = r1.mergeWith(r2);
        assertAll("merge " + r1 + " with " + r2,
                () -> assertEquals(result.getLow(), 100, "lower bound"),
                () -> assertEquals(result.getHigh(), 250, "upper bound")
        );
    }

    @Test
    public void testMergeWithSuperset() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(150, 200);
        Range result = r2.mergeWith(r1);
        assertAll("merge " + r2 + " with " + r1,
                () -> assertEquals(result.getLow(), 100, "lower bound"),
                () -> assertEquals(result.getHigh(), 250, "upper bound")
        );
    }

    @Test
    public void testMergeWithSameLow() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(100, 200);
        Range result = r1.mergeWith(r2);
        assertAll("merge " + r1 + " with " + r2,
                () -> assertEquals(result.getLow(), 100, "lower bound"),
                () -> assertEquals(result.getHigh(), 250, "upper bound")
        );
    }

    @Test
    public void testMergeWithSameHigh() {
        Range r1 = new Range(150, 250);
        Range r2 = new Range(100, 250);
        Range result = r1.mergeWith(r2);
        assertAll("merge " + r1 + " with " + r2,
                () -> assertEquals(result.getLow(), 100, "lower bound"),
                () -> assertEquals(result.getHigh(), 250, "upper bound")
        );
    }

    @Test
    public void testCompareToLowerLow() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(150, 250);
        int result = r1.compareTo(r2);
        assertTrue((result < 0), "r1 not lower");
    }

    @Test
    public void testCompareToHigherLow() {
        Range r1 = new Range(150, 250);
        Range r2 = new Range(100, 250);
        int result = r1.compareTo(r2);
        assertTrue((result > 0), "r1 not higher");
    }

    @Test
    public void testCompareToLowerHigh() {
        Range r1 = new Range(100, 200);
        Range r2 = new Range(100, 250);
        int result = r1.compareTo(r2);
        assertTrue((result < 0), "r1 not lower");
    }

    @Test
    public void testCompareToHigherHigh() {
        Range r1 = new Range(100, 250);
        Range r2 = new Range(100, 200);
        int result = r1.compareTo(r2);
        assertTrue((result > 0), "r1 not higher");
    }

    @Test
    public void testCompareToEqual() {
        Range r1 = new Range(100, 200);
        Range r2 = new Range(100, 200);
        int result = r1.compareTo(r2);
        assertTrue((result == 0), "r1 not equal");
    }
}
