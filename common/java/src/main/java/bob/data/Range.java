package bob.data;

import bob.util.Assert;

/**
 * A pair of longs representing a range of values.
 */
public class Range extends Pair<Long, Long> implements Comparable<Range>{

    /**
     * Normal constructor. The lower bound must be no greater than the upper bound.
     * @param low The range's lower bound
     * @param high The range's upper bound
     */
    public Range(long low, long high) {
        super(low, high);
        Assert.that((low <= high), "Inverted range");
    }
    
    /**
     * Parse two numbers separated with a "-" into a Range object.
     * @param text The text from the puzzle input
     * @return The Range
     */
    public static Range parse(String text) {
        return parse(text, "-");
    }
    
    /**
     * Parse two numbers separated with a delimiter into a Range object.
     * @param text The text from the puzzle input
     * @param delim The delimiter
     * @return The Range
     */
    public static Range parse(String text, String delim) {
        Assert.that(text.contains(delim), "Missing delimiter");
        String[] bits = text.split(delim);
        Assert.that((bits.length == 2), "Invalid data format");
        Assert.that((!bits[0].isBlank()), "Blank lower bound");
        Assert.that((!bits[1].isBlank()), "Blank upper bound");
        return new Range(Long.parseLong(bits[0]), Long.parseLong(bits[1]));
    }

    /**
     * Retrieve the range's lower bound.
     * @return The range's lower bound
     */
    public long getLow() {
        return getFirst();
    }

    /**
     * Retrieve the range's upper bound.
     * @return The range's upper bound
     */
    public long getHigh() {
        return getSecond();
    }
    
    /**
     * Determine if the ranges overlap.
     * @param that The range to check
     * @return {@code true} if the ranges overlap
     */
    public boolean overlaps(Range that) {
        // That range is completely below this range
        if (that.getHigh() < this.getLow()) {
            return false;
        }
        
        // That range is completely above this range
        if (that.getLow() > this.getHigh()) {
            return false;
        }
        
        // That range must overlap this range
        return true;
    }
    
    /**
     * Merge this range with that range.
     * @param that The range to merge
     * @return A new Range with the merged bounds
     */
    public Range mergeWith(Range that) {
        Assert.that(this.overlaps(that), "Merging ranges that do not overlap");
        long low = Math.min(this.getLow(), that.getLow());
        long high = Math.max(this.getHigh(), that.getHigh());
        return new Range(low, high);
    }

    @Override
    public int compareTo(Range that) {
        if (this.getLow() != that.getLow()) {
            return this.getFirst().compareTo(that.getFirst());
        }
        if (this.getHigh() != that.getHigh()) {
            return this.getSecond().compareTo(that.getSecond());
        }
        return 0;
    }

    @Override
    public String toString() {
        return "[" + getLow() + ".." + getHigh() + "]";
    }
}
