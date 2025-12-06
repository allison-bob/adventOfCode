package bob.aoc;

import lombok.Getter;

/**
 * The command in the input data.
 */
@Getter
public class Day02Command {

    private final Day02Direction direction;
    private final int distance;

    public Day02Command(String line) {
        String[] bits = line.split(" ");
        direction = Day02Direction.valueOf(bits[0].toUpperCase());
        distance = Integer.parseInt(bits[1]);
    }
}
