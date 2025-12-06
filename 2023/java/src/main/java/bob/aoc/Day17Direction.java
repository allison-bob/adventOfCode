package bob.aoc;

import bob.data.coordinate.Coord2D;
import lombok.Getter;

@Getter
public enum Day17Direction {
    N(0, -1),
    S(0, 1),
    W(-1, 0),
    E(1, 0);

    public final int dx;
    public final int dy;
    public final Coord2D offset;
    public Day17Direction reverse;

    private Day17Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
        this.offset = new Coord2D(dx, dy);
    }
    
    static {
        N.reverse = S;
        S.reverse = N;
        W.reverse = E;
        E.reverse = W;
    }
}
