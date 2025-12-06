package bob.data.coordinate;

/**
 * A single coordinate in 2D space.
 * <p>
 * Two coordinate objects are equal if they contain the same coordinate values.
 * </p>
 */
public class Coord2D extends CoordBase<Coord2D, Coord1D> {

    /**
     * Normal constructor.
     *
     * @param downDim The coordinate values for the down dimension (one fewer dimensions)
     * @param thisDim The coordinate value for this dimension
     */
    public Coord2D(Coord1D downDim, int thisDim) {
        super(downDim, thisDim, Coord2D.class, Coord1D.class);
    }

    /**
     * Constructor with individual dimension parameters. This constructor simplifies object creation when used within
     * loops that traverse a grid.
     *
     * @param x The X coordinate
     * @param y The Y coordinate
     */
    public Coord2D(int x, int y) {
        this(new Coord1D(x), y);
    }

    /**
     * Retrieve the X coordinate.
     * @return The X coordinate
     */
    public int getX() {
        return getDownDim().getThisDim();
    }

    /**
     * Retrieve the Y coordinate.
     * @return The Y coordinate
     */
    public int getY() {
        return getThisDim();
    }
}
