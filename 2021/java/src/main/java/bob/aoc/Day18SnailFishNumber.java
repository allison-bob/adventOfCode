package bob.aoc;

import bob.util.Assert;
import java.io.ByteArrayInputStream;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Day18SnailFishNumber {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private Integer leftInt;
    private Day18SnailFishNumber leftSFN;
    private Integer rightInt;
    private Day18SnailFishNumber rightSFN;

    public Day18SnailFishNumber(Day18SnailFishNumber toCopy) {
        this.leftInt = toCopy.leftInt;
        this.leftSFN = (toCopy.leftSFN != null) ? new Day18SnailFishNumber(toCopy.leftSFN) : null;
        this.rightInt = toCopy.rightInt;
        this.rightSFN = (toCopy.rightSFN != null) ? new Day18SnailFishNumber(toCopy.rightSFN) : null;
    }

    public Day18SnailFishNumber(int leftInt, int rightInt) {
        this.leftInt = leftInt;
        this.leftSFN = null;
        this.rightInt = rightInt;
        this.rightSFN = null;
    }

    public Day18SnailFishNumber(Day18SnailFishNumber leftSFN, Day18SnailFishNumber rightSFN) {
        this.leftInt = null;
        this.leftSFN = new Day18SnailFishNumber(leftSFN);
        this.rightInt = null;
        this.rightSFN = new Day18SnailFishNumber(rightSFN);
    }

    public Day18SnailFishNumber(ByteArrayInputStream data) {
        Assert.that((data.read() == (int) '['), "First character not [");
        data.mark(2);
        if (data.read() == (int) '[') {
            data.reset();
            leftInt = null;
            leftSFN = new Day18SnailFishNumber(data);
        } else {
            data.reset();
            leftInt = data.read() - '0';
            leftSFN = null;
        }
        Assert.that((data.read() == (int) ','), "Delim character not ,");
        data.mark(2);
        if (data.read() == (int) '[') {
            data.reset();
            rightInt = null;
            rightSFN = new Day18SnailFishNumber(data);
        } else {
            data.reset();
            rightInt = data.read() - '0';
            rightSFN = null;
        }
        Assert.that((data.read() == (int) ']'), "Last character not ]");
    }

    public void reduce() {
        boolean repeat = true;
        while (repeat) {
            repeat = (explode(this, 0) != null);
            if (!repeat) {
                repeat = split(this);
            }
        }
    }

    public long magnitude() {
        return magnitude(this);
    }

    private long magnitude(Day18SnailFishNumber in) {
        long left;
        long right;

        if (in.leftInt != null) {
            left = in.leftInt;
        } else {
            left = magnitude(in.leftSFN);
        }

        if (in.rightInt != null) {
            right = in.rightInt;
        } else {
            right = magnitude(in.rightSFN);
        }

        return (3 * left) + (2 * right);
    }
    
    private Day18SnailFishNumber explode(Day18SnailFishNumber toCheck, int level) {
        LOG.debug("{} explode? {}", level, toCheck);
        Assert.that((level < 5), "level rose above 4!?!?!?!?!");
        
        if (level == 4) {
            // Explode this number
            LOG.debug("{} explode!", level);
            return toCheck;
        }
        
        // If the left part is a SFN, check to see if it needs to explode
        if (toCheck.leftSFN != null) {
            Day18SnailFishNumber result = explode(toCheck.leftSFN, (level + 1));
            LOG.debug("{} L result: {}", level, result);
            if (result != null) {
                if (level == 3) {
                    // Replace the exploded pair with a 0
                    toCheck.leftSFN = null;
                    toCheck.leftInt = 0;
                } else {
                    explodeShiftLeft(toCheck, result);
                }
                explodeShiftRight(toCheck, result);
                explodeShiftNextLeft(toCheck, result);
                // We had an explosion so we are done checking
                return result;
            }
        }
        
        // If the right part is a SFN, check to see if it needs to explode
        if (toCheck.rightSFN != null) {
            Day18SnailFishNumber result = explode(toCheck.rightSFN, (level + 1));
            LOG.debug("{} R result: {}", level, result);
            if (result != null) {
                if (level == 3) {
                    // Replace the exploded pair with a 0
                    toCheck.rightSFN = null;
                    toCheck.rightInt = 0;
                } else {
                    explodeShiftRight(toCheck, result);
                }
                explodeShiftLeft(toCheck, result);
                explodeShiftNextRight(toCheck, result);
                // We had an explosion so we are done checking
                return result;
            }
        }
        
        // No explosions here
        return null;
    }

    private void explodeShiftLeft(Day18SnailFishNumber num, Day18SnailFishNumber result) {
        if ((result.leftInt != null) && (num.leftInt != null)) {
            LOG.debug("    explodeShiftLeft   {}   {}", num, result);
            num.leftInt += result.leftInt;
            // Set result int to null so we know the value has already been added somewhere
            result.leftInt = null;
            LOG.debug("    ... shifted to   {}   {}", num, result);
        }
    }

    private void explodeShiftNextLeft(Day18SnailFishNumber num, Day18SnailFishNumber result) {
        if ((result.rightInt != null) && (num.rightSFN != null)) {
            LOG.debug("    explodeShiftNextLeft   {}   {}", num, result);
            Day18SnailFishNumber target = num.rightSFN;
            while (target.leftInt == null) {
                target = target.leftSFN;
            }
            target.leftInt += result.rightInt;
            result.rightInt = null;
            LOG.debug("    ... shifted to   {}   {}", num, result);
        }
    }

    private void explodeShiftNextRight(Day18SnailFishNumber num, Day18SnailFishNumber result) {
        if ((result.leftInt != null) && (num.leftSFN != null)) {
            LOG.debug("    explodeShiftNextRight   {}   {}", num, result);
            Day18SnailFishNumber target = num.leftSFN;
            while (target.rightInt == null) {
                target = target.rightSFN;
            }
            target.rightInt += result.leftInt;
            result.leftInt = null;
            LOG.debug("    ... shifted to   {}   {}", num, result);
        }
    }

    private void explodeShiftRight(Day18SnailFishNumber num, Day18SnailFishNumber result) {
        if ((result.rightInt != null) && (num.rightInt != null)) {
            LOG.debug("    explodeShiftRight   {}   {}", num, result);
            num.rightInt += result.rightInt;
            result.rightInt = null;
            LOG.debug("    ... shifted to   {}   {}", num, result);
        }
    }
    
    private boolean split(Day18SnailFishNumber toCheck) {
        if (toCheck.leftInt != null) {
            if (toCheck.leftInt > 9) {
                toCheck.leftSFN = new Day18SnailFishNumber((toCheck.leftInt / 2), ((toCheck.leftInt + 1) / 2));
                toCheck.leftInt = null;
                return true;
            }
        } else {
            if (split(toCheck.leftSFN)) {
                return true;
            }
        }
        if (toCheck.rightInt != null) {
            if (toCheck.rightInt > 9) {
                toCheck.rightSFN = new Day18SnailFishNumber((toCheck.rightInt / 2), ((toCheck.rightInt + 1) / 2));
                toCheck.rightInt = null;
                return true;
            }
        } else {
            if (split(toCheck.rightSFN)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (leftSFN != null) {
            sb.append(leftSFN);
        } else {
            sb.append(leftInt);
        }
        sb.append(",");
        if (rightSFN != null) {
            sb.append(rightSFN);
        } else {
            sb.append(rightInt);
        }
        sb.append("]");
        return sb.toString();
    }
}
