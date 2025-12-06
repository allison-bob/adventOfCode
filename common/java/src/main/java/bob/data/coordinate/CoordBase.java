package bob.data.coordinate;

import bob.util.ObjectBuilder;
import java.lang.reflect.Constructor;
import java.util.Objects;
import lombok.Getter;

/**
 * Base class for all coordinate classes.
 *
 * @param <C> This coordinate class
 * @param <D> The coordinate class with one fewer dimensions
 */
public class CoordBase<C extends CoordBase, D extends CoordBase> implements Coord<C, D> {

    private final Constructor<C> cConstruct;

    /**
     * @return The coordinate values for the down dimension (one fewer dimensions), {@code null} if no down dimension
     */
    @Getter
    private final D downDim;

    /**
     * @return The coordinate value for this dimension
     */
    @Getter
    private final int thisDim;

    /**
     * Normal constructor. It is expected for each subclass to also have a constructor with individual coordinate values
     * for simpler use in loops, etc.
     *
     * @param downDim The coordinate values for the down dimension (one fewer dimensions)
     * @param thisDim The coordinate value for this dimension
     * @param cClass The class object for the subclass
     * @param dClass The class object for the down-dimension subclass
     */
    public CoordBase(D downDim, int thisDim, Class<C> cClass, Class<D> dClass) {
        this.downDim = downDim;
        this.thisDim = thisDim;
        this.cConstruct = ObjectBuilder.getConstructor(cClass, dClass, int.class);
    }

    @Override
    public C addOffset(int offset) {
        if (getDownDim() == null) {
            return ObjectBuilder.getInstance(cConstruct, null, (getThisDim() + offset));
        } else {
            return ObjectBuilder.getInstance(cConstruct, getDownDim().addOffset(offset), (getThisDim() + offset));
        }
    }

    @Override
    public C addOffset(C offset) {
        if (getDownDim() == null) {
            return ObjectBuilder.getInstance(cConstruct, null, (getThisDim() + offset.getThisDim()));
        } else {
            return ObjectBuilder.getInstance(cConstruct, getDownDim().addOffset(offset.getDownDim()),
                    (getThisDim() + offset.getThisDim()));
        }
    }

    @Override
    public int compareTo(C other) {
        int result = Integer.compare(this.getThisDim(), other.getThisDim());
        if (getDownDim() != null) {
            if (result == 0) {
                result = getDownDim().compareTo(other.getDownDim());
            }
        }
        return result;
    }

    @Override
    public boolean isNotNegative() {
        if (getThisDim() < 0) {
            return false;
        }
        if (getDownDim() == null) {
            return true;
        } else {
            return getDownDim().isNotNegative();
        }
    }

    @Override
    public int manhattan() {
        int result = Math.abs(getThisDim());
        if (getDownDim() != null) {
            result += getDownDim().manhattan();
        }
        return result;
    }

    @Override
    public int manhattan(C location) {
        int result = Math.abs(getThisDim() - location.getThisDim());
        if (getDownDim() != null) {
            result += getDownDim().manhattan(location.getDownDim());
        }
        return result;
    }

    @Override
    public String values() {
        return valueBuilder().toString();
    }

    protected StringBuilder valueBuilder() {
        StringBuilder sb;
        if (getDownDim() == null) {
            sb = new StringBuilder();
        } else {
            sb = getDownDim().valueBuilder().append(",");
        }
        return sb.append(getThisDim());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + getThisDim();
        if (getDownDim() != null) {
            hash += getDownDim().hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CoordBase<?, ?> other = (CoordBase<?, ?>) obj;
        if (this.thisDim != other.thisDim) {
            return false;
        }
        return Objects.equals(this.downDim, other.downDim);
    }

    @Override
    public String toString() {
        return valueBuilder().insert(0, "(").append(")").toString();
    }
}
