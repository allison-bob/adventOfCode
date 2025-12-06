package bob.aoc;

import bob.util.Assert;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day23CupRing {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    // Index is the ID, value is the next ID in the ring
    @Getter
    private final int[] ring;

    // Pointer to the current ID
    private int curr = -1;

    // Pointer to last ID added
    private int loadpt = -1;

    public Day23CupRing(int length) {
        // Array has extra element since element 0 is not used
        ring = new int[length + 1];
    }

    public void add(String line) {
        Assert.that((line.length() == 9), "Wrong line length");
        for (int i = 0; i < line.length(); i++) {
            add(Integer.parseInt(line.substring(i, i + 1)));
        }
        fillFrom(10);
    }

    public void add(int newval) {
        if (loadpt == -1) {
            loadpt = newval;
            curr = newval;
        } else {
            ring[loadpt] = newval;
            loadpt = newval;
        }
        //LOG.debug("Added {}, loadpt={}, {}", newval, loadpt, toString());
    }

    public void fillFrom(int nextval) {
        // Fill all but the last element sequentially
        if (nextval < ring.length) {
            ring[loadpt] = nextval;
            //LOG.debug("Filled {}, loadpt={}, {}", nextval, loadpt, toString());
            for (loadpt = nextval; loadpt < ring.length - 1; loadpt++) {
                ring[loadpt] = loadpt + 1;
                //LOG.debug("Filled {}, loadpt={}, {}", loadpt + 1, loadpt, toString());
            }
        }

        // Point the last element back to the first
        ring[loadpt] = curr;
        //LOG.debug("Filled {}, loadpt={}, {}", curr, loadpt, toString());
    }

    public void moveCups() {
        // Determine the cups to pull
        int pull1 = ring[curr];
        int pull2 = ring[pull1];
        int pull3 = ring[pull2];
        int notpulled = ring[pull3];
        LOG.debug("curr " + curr + ", pulled: " + pull1 + ", " + pull2 + ", " + pull3 + " leaving " + notpulled);

        // Find the destination
        int dest = (curr == 1) ? (ring.length - 1) : (curr - 1);
        while ((dest == pull1) || (dest == pull2) || (dest == pull3)) {
            dest = (dest == 1) ? (ring.length - 1) : (dest - 1);
        }
        LOG.debug("destination: {}, {}", dest, ring[dest]);

        // Link the current cup to the first one not pulled
        ring[curr] = notpulled;

        // Insert the pulled cups after the destination
        int afterdest = ring[dest];
        ring[dest] = pull1;
        ring[pull3] = afterdest;

        // Shift the current cup
        curr = ring[curr];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = curr;
        sb.append(i);
        while ((ring[i] != curr) && (ring[i] != 0)) {
            i = ring[i];
            sb.append(",").append(i);
        }
        return "curr=" + curr + ", ring is " + sb.toString();
    }
}
