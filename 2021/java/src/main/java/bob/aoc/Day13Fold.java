package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public class Day13Fold {

    public enum Direction {
        X, Y
    }

    private final Direction axis;
    private final int location;

    public Day13Fold(String line) {
        Assert.that(line.startsWith("fold along "), "instruction syntax error");

        this.axis = Direction.valueOf(line.substring(11, 12).toUpperCase());
        this.location = Integer.parseInt(line.substring(13));
    }
}
