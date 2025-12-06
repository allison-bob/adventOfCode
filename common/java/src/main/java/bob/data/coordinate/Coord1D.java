package bob.data.coordinate;

/**
 * A single coordinate in 1D space.
 * <p>
 * Two coordinate objects are equal if they contain the same coordinate values.
 * </p>
 */
public class Coord1D extends CoordBase<Coord1D, Coord1D> {

    /**
     * Normal constructor. Note that the value specified for {@code downDim} is ignored since there is no down dimension
     * from here.
     *
     * @param downDim The coordinate values for the down dimension (one fewer dimensions)
     * @param thisDim The coordinate value for this dimension
     */
    public Coord1D(Coord1D downDim, int thisDim) {
        // There is no down dimension from here, so it is set to null. This is used as an indicator that a different
        // code path is required in the base class.
        super(null, thisDim, Coord1D.class, Coord1D.class);
    }

    /**
     * Constructor with individual dimension parameters. This constructor simplifies object creation when used within
     * loops that traverse a grid.
     *
     * @param x The X coordinate
     */
    public Coord1D(int x) {
        this(null, x);
    }

    /**
     * Retrieve the X coordinate.
     * @return The X coordinate
     */
    public int getX() {
        return getThisDim();
    }
}
