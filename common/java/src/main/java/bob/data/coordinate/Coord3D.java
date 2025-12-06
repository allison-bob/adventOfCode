package bob.data.coordinate;

/**
 * A single coordinate in 3D space.
 * <p>
 * Two coordinate objects are equal if they contain the same coordinate values.
 * </p>
 */
public class Coord3D extends CoordBase<Coord3D, Coord2D> {

    /**
     * Normal constructor.
     *
     * @param downDim The coordinate values for the down dimension (one fewer dimensions)
     * @param thisDim The coordinate value for this dimension
     */
    public Coord3D(Coord2D downDim, int thisDim) {
        super(downDim, thisDim, Coord3D.class, Coord2D.class);
    }

    /**
     * Constructor with individual dimension parameters. This constructor simplifies object creation when used within
     * loops that traverse a grid.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     */
    public Coord3D(int x, int y, int z) {
        this(new Coord2D(x, y), z);
    }

    /**
     * Retrieve the X coordinate.
     * @return The X coordinate
     */
    public int getX() {
        return getDownDim().getDownDim().getThisDim();
    }

    /**
     * Retrieve the Y coordinate.
     * @return The Y coordinate
     */
    public int getY() {
        return getDownDim().getThisDim();
    }

    /**
     * Retrieve the Z coordinate.
     * @return The Z coordinate
     */
    public int getZ() {
        return getThisDim();
    }
}
