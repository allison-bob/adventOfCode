package bob.aoc;

import java.util.Arrays;

public class Day08Step {

    public String id;
    public String[] next;

    public Day08Step(String line) {
        String[] bits = line.replaceAll("[=(),]", "").split(" ");
        id = bits[0];
        next = new String[] {bits[2], bits[3]};
    }

    @Override
    public String toString() {
        return id + ":" + Arrays.toString(next);
    }
}
