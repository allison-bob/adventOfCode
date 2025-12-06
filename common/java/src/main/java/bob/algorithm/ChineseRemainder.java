package bob.algorithm;

import bob.util.Assert;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an implementation of the Chinese Remainder Theorem (CRT). The theorem is described in
 * {@linkplain https://en.wikipedia.org/wiki/Chinese_remainder_theorem Wikipedia}.
 * <p>
 * This theorem is based on {@linkplain https://en.wikipedia.org/wiki/Modular_arithmetic modular arithmetic} which is
 * like counting hours on a 12-hour clock. When you pass 12:00, the next hour is 1:00; instead of the next hour being
 * 13, the numbers wrapped around and returned to 1.
 * </p>
 * <p>
 * Modular arithmetic statements are congruences instead of equations, written as "a <u>=</u> b (mod n)" which states
 * that (a % n) == (b % n). Note that this does not mean that a == b; the relationship between a and b can be expressed
 * in several ways:
 * </p>
 * <ul>
 * <li>a = kn + b</li>
 * <li>a - b = kn</li>
 * <li>a = pn + r, b = qn + r</li>
 * </ul>
 * <p>
 * A video that explains this in more detail can be found on YouTube from
 * {@linkplain https://www.youtube.com/watch?v=5OjZWSdxlU0 tomrocksmaths}.
 * </p>
 * <p>
 * CRT is used to solve a set of congruences, finding a single value that satisfies everything in the set. For example
 * (taken from a video by {@linkplain https://www.youtube.com/watch?v=zIFehsBHB8o Maths with Jay}):
 * </p>
 * <ul>
 * <li>x <u>=</u> b<sub>1</sub> (mod n<sub>1</sub>)</li>
 * <li>x <u>=</u> b<sub>2</sub> (mod n<sub>2</sub>)</li>
 * <li>x <u>=</u> b<sub>3</sub> (mod n<sub>3</sub>)</li>
 * </ul>
 * <p>
 * Define N as the product of all the moduli (n<sub>1</sub>, n<sub>2</sub>, n<sub>3</sub>). To find the value of x, you
 * first need to get three values for each congruence in the set: the remainder (b<sub>i</sub>), a multiplication factor
 * for all other congruences (N<sub>i</sub> = N / n<sub>i</sub>), and the inverse of N<sub>i</sub> (x<sub>i</sub>). Once
 * these are found for each congruence, the three values are multiplied together (b<sub>i</sub> * N<sub>i</sub> *
 * x<sub>i</sub>); the desired x is the sum of these products.
 * </p>
 * <p>
 * The inverse of N<sub>i</sub>, x<sub>i</sub>, is a number such that (N<sub>i</sub> * x<sub>i</sub>) <u>=</u> 1 (mod
 * n<sub>i</sub>). Most of the CRT videos work with small numbers and find the inverse by trial and error. For large
 * numbers, we will need to use the Extended Euclidean Algorithm to find the inverse. A video that describes this
 * process in the context of cryptography is available from
 * {@linkplain https://www.youtube.com/watch?v=_bRVA5b4sb4 GVSUmath}. Another video, purely a tutorial on finding the
 * inverse, is available from {@linkplain https://www.youtube.com/watch?v=fz1vxq5ts5I Emily S}. For now, I am assuming
 * that the moduli will be small enough that we can use trial and error to find the inverse.
 * </p>
 */
public class ChineseRemainder {

    public static class Congruence {

        public final int remainder;
        public final int modulus;
        public long multfactor;
        public int mfinverse;

        public Congruence(int remainder, int modulus) {
            Assert.that((modulus > 1), "modulus must > 1");
            int r = remainder;
            while (r < 0) {
                r += modulus;
            }
            this.remainder = r;
            this.modulus = modulus;
        }

        @Override
        public String toString() {
            return "x = " + remainder + " (mod " + modulus + "):" + multfactor + "," + mfinverse;
        }
    }

    public final List<Congruence> congruences = new ArrayList<>();
    public long modproduct;

    public void addCongruence(int remainder, int modulus) {
        congruences.add(new Congruence(remainder, modulus));
    }

    public long solve() {
        // Find the product of all moduli
        modproduct = congruences.stream()
                .mapToLong(c -> c.modulus)
                .reduce(1L, Math::multiplyExact);

        // Find the multiplcation factor for each congruence
        for (Congruence c : congruences) {
            c.multfactor = modproduct / c.modulus;
        }

        // Find the inverse for each congruence
        for (Congruence c : congruences) {
            for (int i = 1; ((i < c.modulus) && (c.mfinverse == 0)); i++) {
                if (((i * c.multfactor) % c.modulus) == c.remainder) {
                    c.mfinverse = i;
                }
            }
        }

        // Compute the answer
        long result = congruences.stream()
                .mapToLong(c -> c.mfinverse * c.multfactor)
                .sum();
        return result % modproduct;
    }
}
