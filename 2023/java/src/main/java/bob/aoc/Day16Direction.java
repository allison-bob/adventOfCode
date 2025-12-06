package bob.aoc;

import lombok.Getter;

@Getter
public enum Day16Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    private Day16Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
