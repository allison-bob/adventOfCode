package bob.aoc;

import bob.data.coordinate.Coord2D;
import lombok.Getter;

@Getter
public class Day17State {
    public final Coord2D location;
    public final Day17Direction direction;
    public final int linelen;

    public Day17State(Coord2D location, Day17Direction direction, int linelen) {
        this.location = location;
        this.direction = direction;
        this.linelen = linelen;
    }

    @Override
    public String toString() {
        return "(" + location + ":" + direction + "*" + linelen + ")";
    }
}
