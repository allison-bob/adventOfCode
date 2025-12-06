package bob.data.coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Map 3D coordinates based on rotations around the three axes.
 * <p>
 * Enum names are two letters indicating the direction of the X axis, Y axis, and Z axis. Initial mapping is X=East,
 * Y=North, Z=Up
 * </p>
 * <p>
 * Direction names are: North, South, East, West, Up, Down
 * </p>
 */
public enum Mapper3D implements Mapper<Coord3D> {
    // Each group is the X axis pointing a certain direction then rotating around the X axis
    //
    // Y + 000, Z + 000
    ENU(p -> p),
    EDN(p -> new Coord3D(p.getX(), -p.getZ(), p.getY())),
    ESD(p -> new Coord3D(p.getX(), -p.getY(), -p.getZ())),
    EUS(p -> new Coord3D(p.getX(), p.getZ(), -p.getY())),
    // Y + 090, Z + 000
    UNW(p -> new Coord3D(-p.getZ(), p.getY(), p.getX())),
    UEN(p -> new Coord3D(-p.getZ(), -p.getX(), p.getY())),
    USE(p -> new Coord3D(-p.getZ(), -p.getY(), -p.getX())),
    UWS(p -> new Coord3D(-p.getZ(), p.getX(), -p.getY())),
    // Y + 180, Z + 000
    WND(p -> new Coord3D(-p.getX(), p.getY(), -p.getZ())),
    WUN(p -> new Coord3D(-p.getX(), p.getZ(), p.getY())),
    WSU(p -> new Coord3D(-p.getX(), -p.getY(), p.getZ())),
    WDS(p -> new Coord3D(-p.getX(), -p.getZ(), -p.getY())),
    // Y + 270, Z + 000
    DSE(p -> new Coord3D(p.getZ(), p.getY(), -p.getX())),
    DWS(p -> new Coord3D(p.getZ(), p.getX(), p.getY())),
    DNW(p -> new Coord3D(p.getZ(), -p.getY(), p.getX())),
    DEN(p -> new Coord3D(p.getZ(), -p.getX(), -p.getY())),
    // Y + 000, Z + 090
    NWU(p -> new Coord3D(p.getY(), -p.getX(), p.getZ())),
    NDW(p -> new Coord3D(p.getY(), -p.getZ(), -p.getX())),
    NED(p -> new Coord3D(p.getY(), p.getX(), -p.getZ())),
    NUE(p -> new Coord3D(p.getY(), p.getZ(), p.getX())),
    // Y + 000, Z + 270
    SEU(p -> new Coord3D(-p.getY(), p.getX(), p.getZ())),
    SDE(p -> new Coord3D(-p.getY(), -p.getZ(), p.getX())),
    SWD(p -> new Coord3D(-p.getY(), -p.getX(), -p.getZ())),
    SUW(p -> new Coord3D(-p.getY(), p.getZ(), -p.getX()));

    private final Function<Coord3D, Coord3D> mapper;

    private static final Map<Mapper3D, Mapper3D> inverses = new HashMap<>();

    static {
        inverses.put(ENU, ENU);
        inverses.put(EDN, EUS);
        inverses.put(ESD, ESD);
        inverses.put(EUS, EDN);
        inverses.put(UNW, DSE);
        inverses.put(UEN, SUW);
        inverses.put(USE, USE);
        inverses.put(UWS, NDW);
        inverses.put(WND, WND);
        inverses.put(WUN, WUN);
        inverses.put(WSU, WSU);
        inverses.put(WDS, WDS);
        inverses.put(DSE, UNW);
        inverses.put(DWS, NUE);
        inverses.put(DNW, DNW);
        inverses.put(DEN, SDE);
        inverses.put(NWU, SEU);
        inverses.put(NDW, UWS);
        inverses.put(NED, NED);
        inverses.put(NUE, DWS);
        inverses.put(SEU, NWU);
        inverses.put(SDE, DEN);
        inverses.put(SWD, SWD);
        inverses.put(SUW, UEN);
    }

    private Mapper3D(Function<Coord3D, Coord3D> mapper) {
        this.mapper = mapper;
    }
    
    // For testing the inverse mapping
    Mapper3D inverse() {
        return inverses.get(this);
    }

    @Override
    public Coord3D map(Coord3D origCoord) {
        return mapper.apply(origCoord);
    }

    @Override
    public Coord3D unmap(Coord3D mapCoord) {
        return inverses.get(this).map(mapCoord);
    }

    @Override
    public Coord3D mapSize(Coord3D origSize) {
        Coord3D mapped = map(origSize);
        return new Coord3D(Math.abs(mapped.getX()), Math.abs(mapped.getY()), Math.abs(mapped.getZ()));
    }
}
