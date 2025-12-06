package bob.aoc;

import bob.util.Assert;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Day04Assignment {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final int aLow;
    private final int aHigh;
    private final int bLow;
    private final int bHigh;

    public Day04Assignment(String line) {
        String[] bits = line.split("[-,]");
        Assert.that((bits.length == 4), "input line field count");
        this.aLow = Integer.parseInt(bits[0]);
        this.aHigh = Integer.parseInt(bits[1]);
        this.bLow = Integer.parseInt(bits[2]);
        this.bHigh = Integer.parseInt(bits[3]);
    }
    
    public boolean fullOverlap() {
        boolean result = false;
        
        // A entirely contained in B
        result |= ((aLow >= bLow) && (aHigh <= bHigh));
        
        // B entirely contained in A
        result |= ((bLow >= aLow) && (bHigh <= aHigh));
        
        return result;
    }
    
    public boolean partialOverlap() {
        LOG.debug("Checking {} - {}, {} - {}", aLow, aHigh, bLow, bHigh);
        boolean result = false;
        
        // A contains B's low bound
        result |= ((aLow <= bLow) && (aHigh >= bLow));
        LOG.debug("   at 1, result is {}", result);
        
        // A contains B's high bound
        result |= ((aLow <= bHigh) && (aHigh >= bHigh));
        LOG.debug("   at 2, result is {}", result);
        
        // B contains A's low bound
        result |= ((bLow <= aLow) && (bHigh >= aLow));
        LOG.debug("   at 3, result is {}", result);
        
        // B contains A's high bound
        result |= ((bLow <= aHigh) && (bHigh >= aHigh));
        LOG.debug("   at 4, result is {}", result);
        
        return result;
    }
}
