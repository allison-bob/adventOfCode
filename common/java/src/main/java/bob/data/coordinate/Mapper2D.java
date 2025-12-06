package bob.data.coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Map 2D coordinates based on various permutations of rotation and flips.
 * <p>
 * Enum names are two letters indicating the direction of the X axis and Y axis. Initial mapping is X=East, Y=North
 * </p>
 * <p>
 * Direction names are: North, South, East, West
 * </p>
 */
public enum Mapper2D implements Mapper<Coord2D> {
    //  . . | A .
    //  . . B . C
    //  - - + D -
    //  . . | . .
    //  . . | . .
    EN(p -> new Coord2D(p.getX(), p.getY())),
    //  . . | . .
    //  . . | . .
    //  - - + D -
    //  . . B . C
    //  . . | A .
    ES(p -> new Coord2D(p.getX(), -p.getY())),
    //  . . | C .
    //  . . D . A
    //  - - + B -
    //  . . | . .
    //  . . | . .
    NE(p -> new Coord2D(p.getY(), p.getX())),
    //  . C | . .
    //  A . D . .
    //  - B + - -
    //  . . | . .
    //  . . | . .
    NW(p -> new Coord2D(-p.getY(), p.getX())),
    //  . . | . .
    //  . . | . .
    //  - - + B -
    //  . . D . A
    //  . . | C .
    SE(p -> new Coord2D(p.getY(), -p.getX())),
    //  . . | . .
    //  . . | . .
    //  - B + - -
    //  A . D . .
    //  . C | . .
    SW(p -> new Coord2D(-p.getY(), -p.getX())),
    //  . A | . .
    //  C . B . .
    //  - D + - -
    //  . . | . .
    //  . . | . .
    WN(p -> new Coord2D(-p.getX(), p.getY())),
    //  . . | . .
    //  . . | . .
    //  - D + - -
    //  C . B . .
    //  . A | . .
    WS(p -> new Coord2D(-p.getX(), -p.getY()));

    private final Function<Coord2D, Coord2D> mapper;

    private Mapper2D(Function<Coord2D, Coord2D> mapper) {
        this.mapper = mapper;
    }

    private static final Map<Mapper2D, Mapper2D> inverses = new HashMap<>();

    static {
        inverses.put(EN, EN);
        inverses.put(ES, ES);
        inverses.put(NW, SE);
        inverses.put(NE, NE);
        inverses.put(WS, WS);
        inverses.put(WN, WN);
        inverses.put(SE, NW);
        inverses.put(SW, SW);
    }
    
    // For testing the inverse mapping
    Mapper2D inverse() {
        return inverses.get(this);
    }

    @Override
    public Coord2D map(Coord2D origCoord) {
        return mapper.apply(origCoord);
    }

    @Override
    public Coord2D unmap(Coord2D mapCoord) {
        return inverses.get(this).map(mapCoord);
    }

    @Override
    public Coord2D mapSize(Coord2D origSize) {
        Coord2D mapped = map(origSize);
        return new Coord2D(Math.abs(mapped.getX()), Math.abs(mapped.getY()));
    }
}
