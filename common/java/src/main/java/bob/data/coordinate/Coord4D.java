package bob.data.coordinate;

/**
 * A single coordinate in 4D space.
 * <p>
 * Two coordinate objects are equal if they contain the same coordinate values.
 * </p>
 */
public class Coord4D extends CoordBase<Coord4D, Coord3D> {

    /**
     * Normal constructor.
     *
     * @param downDim The coordinate values for the down dimension (one fewer dimensions)
     * @param thisDim The coordinate value for this dimension
     */
    public Coord4D(Coord3D downDim, int thisDim) {
        super(downDim, thisDim, Coord4D.class, Coord3D.class);
    }

    /**
     * Constructor with individual dimension parameters. This constructor simplifies object creation when used within
     * loops that traverse a grid.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @param w The W coordinate
     */
    public Coord4D(int x, int y, int z, int w) {
        this(new Coord3D(x, y, z), w);
    }

    /**
     * Retrieve the X coordinate.
     * @return The X coordinate
     */
    public int getX() {
        return getDownDim().getDownDim().getDownDim().getThisDim();
    }

    /**
     * Retrieve the Y coordinate.
     * @return The Y coordinate
     */
    public int getY() {
        return getDownDim().getDownDim().getThisDim();
    }

    /**
     * Retrieve the Z coordinate.
     * @return The Z coordinate
     */
    public int getZ() {
        return getDownDim().getThisDim();
    }

    /**
     * Retrieve the W coordinate.
     * @return The W coordinate
     */
    public int getW() {
        return getThisDim();
    }
}
