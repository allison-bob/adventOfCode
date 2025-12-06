package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.util.Assert;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum Day10Tile {
    NS('|', 0, -1, 0, 1),
    EW('-', 1, 0, -1, 0),
    NE('L', 0, -1, 1, 0),
    NW('J', 0, -1, -1, 0),
    SE('F', 0, 1, 1, 0),
    SW('7', 0, 1, -1, 0),
    GND('.', 0, 0, 0, 0),
    START('S', 0, 0, 0, 0),
    OUTSIDE('O', 0, 0, 0, 0),
    FILL(' ', 0, 0, 0, 0),
    CURR('*', 0, 0, 0, 0);

    private final char symbol;
    private final Coord2D conn1;
    private final Coord2D conn2;

    private Day10Tile(char symbol, int conn1x, int conn1y, int conn2x, int conn2y) {
        this.symbol = symbol;
        this.conn1 = new Coord2D(conn1x, conn1y);
        this.conn2 = new Coord2D(conn2x, conn2y);
    }

    public static Day10Tile byChar(char in) {
        for (Day10Tile at : Day10Tile.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw Assert.failed(null, "Unknown Tile symbol: " + in);
    }
    
    public static List<Day10Tile> directions() {
        return Stream.of(values())
                .filter(t -> t.conn1.manhattan() > 0)
                .toList();
    }
}
