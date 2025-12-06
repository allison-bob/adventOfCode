package bob.aoc;

import bob.util.Assert;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day03Sack {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final char[] comp1;
    private final char[] comp2;
    @Getter
    private final char[] content;
    
    public Day03Sack(String line) {
        Assert.that(((line.length() % 2) == 0), "Odd-length line");
        int complen = line.length() / 2;
        content = line.toCharArray();
        comp1 = line.substring(0, complen).toCharArray();
        comp2 = line.substring(complen).toCharArray();
        LOG.debug("Compartment 1 contains {}", comp1);
        LOG.debug("Compartment 2 contains {}", comp2);
    }
    
    public char findCommon() {
        for (int i = 0; i < comp1.length; i++) {
            for (int j = 0; j < comp2.length; j++) {
                if (comp1[i] == comp2[j]) {
                    LOG.debug("match at " + i + "," + j + ": " + comp1[i]);
                    return comp1[i];
                }
            }
        }
        throw Assert.failed(null, "No match found");
    }
    
    public static int priority(char c) {
        if (c >= 'a') {
            return c - 'a' + 1;
        } else {
            return c - 'A' + 27;
        }
    }
}
