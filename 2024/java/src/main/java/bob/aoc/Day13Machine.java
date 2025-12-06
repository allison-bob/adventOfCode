package bob.aoc;

import bob.util.Assert;

public class Day13Machine {

    // The buttons, value is the distance the claw moves when pressed
    public long ax;
    public long ay;
    public long bx;
    public long by;
    // Where the prize is located
    public long px;
    public long py;
    // Number of times to push each button
    public long pa;
    public long pb;

    public void accept(String line) {
        if (line.startsWith("Button")) {
            String[] bits = line.split("[ :+,]");
            long x = Long.parseLong(bits[4]);
            long y = Long.parseLong(bits[7]);
            if (bits[1].equals("A")) {
                ax = x;
                ay = y;
            } else {
                bx = x;
                by = y;
            }
        } else if (line.startsWith("Prize")) {
            String[] bits = line.split("[ :=,]");
            px = Long.parseLong(bits[3]);
            py = Long.parseLong(bits[6]);
        } else {
            throw Assert.failed(null, "Unexpected input line: " + line);
        }
    }
    
    public long cost() {
        return (3 * pa) + (1 * pb);
    }

    @Override
    public String toString() {
        return "[A=(" + ax + "," + ay + ")/" + pa + ",B=(" + bx + "," + by + ")/" + pb
                + ",P=(" + px + "," + py + ")]";
    }
}
