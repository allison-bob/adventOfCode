package bob.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AssertTest {

    public AssertTest() {
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
    public void testThatTrue() {
        assertDoesNotThrow(() -> Assert.that(true, "should not fail"));
    }

    @Test
    public void testThatFalse() {
        String msg = "should fail";
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> Assert.that(false, msg));
        assertEquals(msg, thrown.getMessage());
    }

    @Test
    public void testFailed() {
        String msg = "should fail";
        Throwable failure = new Throwable("oops, I failed");
        RuntimeException thrown = Assert.failed(failure, msg);
        assertAll(
                () -> assertEquals(msg, thrown.getMessage()),
                () -> assertEquals(failure, thrown.getCause())
        );
    }
}
