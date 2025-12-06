package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public class Day05Instruction {
    
    private final int count;
    private final int from;
    private final int to;

    public Day05Instruction(String line) {
        String[] bits = line.split(" ");
        Assert.that((bits.length == 6), "Wrong number of input words");
        Assert.that(bits[0].equals("move"), "word 0 not move");
        count = Integer.parseInt(bits[1]);
        Assert.that(bits[2].equals("from"), "word 2 not from");
        from = Integer.parseInt(bits[3]);
        Assert.that(bits[4].equals("to"), "word 4 not to");
        to = Integer.parseInt(bits[5]);
    }

    @Override
    public String toString() {
        return "move " + count + " creates from stack " + from + " to stack " + to;
    }
}
