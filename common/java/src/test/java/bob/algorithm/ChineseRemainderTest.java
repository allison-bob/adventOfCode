package bob.algorithm;

import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChineseRemainderTest {

    public ChineseRemainderTest() {
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
    public void testSampleProblem() {
        // Sample problem from https://www.youtube.com/watch?v=ru7mWZJlRQg
        int[][] expected = new int[][]{
            new int[]{2, 3, 20, 1},
            new int[]{2, 4, 15, 2},
            new int[]{1, 5, 12, 3}
        };
        int expmodprod = Stream.of(expected)
                .mapToInt(e -> e[1])
                .reduce(1, Math::multiplyExact);
        long expanswer = 26;

        ChineseRemainder target = new ChineseRemainder();
        for (int i = 0; i < expected.length; i++) {
            // Gets two negative inputs which should be corrected
            target.addCongruence((expected[i][0] - (i * expected[i][1])), expected[i][1]);
        }

        long result = target.solve();

        assertAll("congruences",
                () -> {
                    assertEquals(expected.length, target.congruences.size(), "number of congruences");

                    for (int i = 0; i < expected.length; i++) {
                        ChineseRemainder.Congruence c = target.congruences.get(i);
                        int[] e = expected[i];

                        assertAll("congruence " + i,
                                () -> {
                                    assertEquals(e[0], c.remainder, "remainder");
                                    assertEquals(e[1], c.modulus, "modulus");
                                    assertEquals(e[2], c.multfactor, "multfactor");
                                    assertEquals(e[3], c.mfinverse, "mfinverse");
                                }
                        );
                    }
                }
        );
    }
}
