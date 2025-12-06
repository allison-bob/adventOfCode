package bob.aoc;

import bob.util.Assert;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Day13Packet {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private Day13PacketData left;
    private Day13PacketData right;
    
    public void addData(String line) {
        if (left == null) {
            left = new Day13PacketData(line);
        } else if (right == null) {
            right = new Day13PacketData(line);
        } else {
            throw Assert.failed(null, "Third line in packet");
        }
    }
    
    public boolean valid() {
        return left.compareTo(right) < 0;
    }

    @Override
    public String toString() {
        return "\nleft =" + left + "\nright=" + right;
    }
}
