package bob.aoc;

import bob.util.Assert;
import java.util.stream.IntStream;
import lombok.Getter;

@Getter
public class Day22Instruction {

    private final boolean turnOn;
    private final int xlo;
    private final int xhi;
    private final int ylo;
    private final int yhi;
    private final int zlo;
    private final int zhi;

    public Day22Instruction(String line) {
        String[] bits = line.split(" |=|\\.\\.|,");
        Assert.that((bits.length == 10), "Wrong input word count");

        turnOn = bits[0].equals("on");

        Assert.that(bits[1].equals("x"), "X label");
        xlo = Integer.parseInt(bits[2]);
        xhi = Integer.parseInt(bits[3]);

        Assert.that(bits[4].equals("y"), "Y label");
        ylo = Integer.parseInt(bits[5]);
        yhi = Integer.parseInt(bits[6]);

        Assert.that(bits[7].equals("z"), "Z label");
        zlo = Integer.parseInt(bits[8]);
        zhi = Integer.parseInt(bits[9]);
    }

    public boolean inInitArea() {
        long inCount = IntStream.of(xlo, xhi, ylo, yhi, zlo, zhi)
                .filter(v -> ((v >= -50) && (v <= 50)))
                .count();
        if (inCount == 0) {
            return false;
        }
        if (inCount == 6) {
            return true;
        }
        throw Assert.failed(null, "Partially in initialization area: " + toString());
    }

    @Override
    public String toString() {
        return "turn " + (turnOn ? "on" : "off") + " cubes in (" + xlo + ".." + xhi + ","
                + ylo + ".." + yhi + "," + zlo + ".." + zhi + ")";
    }
}
