package bob.aoc;

import lombok.Getter;

@Getter
public class Day09Move {

    @Getter
    public enum Direction {
        R(1, 0),
        U(0, 1),
        L(-1, 0),
        D(0, -1);

        private final int dx;
        private final int dy;

        private Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private final Direction dir;
    private final int length;

    public Day09Move(String line) {
        this.dir = Direction.valueOf(line.substring(0, 1));
        this.length = Integer.parseInt(line.substring(2));
    }

    @Override
    public String toString() {
        return dir.name() + " " + length;
    }
}
