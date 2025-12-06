package bob.aoc;

import bob.data.coordinate.Coord2D;
import lombok.Getter;

@Getter
public enum Day23Direction {

    NORTH(new Coord2D(0, -1), new Coord2D(-1, -1), new Coord2D(0, -1), new Coord2D(1, -1)),
    SOUTH(new Coord2D(0, 1), new Coord2D(-1, 1), new Coord2D(0, 1), new Coord2D(1, 1)),
    WEST(new Coord2D(-1, 0), new Coord2D(-1, -1), new Coord2D(-1, 0), new Coord2D(-1, 1)),
    EAST(new Coord2D(1, 0), new Coord2D(1, -1), new Coord2D(1, 0), new Coord2D(1, 1));
    
    private final Coord2D proposal;
    private final Coord2D[] checks;

    private Day23Direction(Coord2D proposal, Coord2D... checks) {
        this.proposal = proposal;
        this.checks = checks;
    }
}
