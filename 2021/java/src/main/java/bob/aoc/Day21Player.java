package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public class Day21Player {

    private final String id;
    private int position;
    private long score;

    public Day21Player(String line) {
        String[] bits = line.split(" ");
        Assert.that((bits.length == 5), "wrong number of words on line");
        id = bits[1];
        position = Integer.parseInt(bits[4]);
    }

    public boolean move(int roll, int winningValue) {
        position = ((position + roll - 1) % 10) + 1;
        score += position;
        return (score >= winningValue);
    }
}
