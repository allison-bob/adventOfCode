package bob.aoc;

import bob.data.graph.Node;
import bob.util.Assert;
import java.util.Arrays;
import lombok.Getter;

@Getter
public class Day16Valve implements Comparable<Day16Valve>, Node<String> {
    
    private final String id;
    private final int flowRate;
    private final String[] tunnelsTo;

    public Day16Valve(String line) {
        String[] bits = line.replaceAll("[,;]", "").replaceAll("[=]", " ").split(" ");
        Assert.that(bits[0].equals("Valve"), "Expected Valve");
        Assert.that(bits[2].equals("has"), "Expected has");
        Assert.that(bits[4].equals("rate"), "Expected rate");
        Assert.that(bits[6].startsWith("tunnel"), "Expected tunnel*");
        Assert.that(bits[9].startsWith("valve"), "Expected valve*");
        
        this.id = bits[1];
        this.flowRate = Integer.parseInt(bits[5]);
        this.tunnelsTo = Arrays.copyOfRange(bits, 10, bits.length);
    }

    public Day16Valve(String id, int flowRate, String[] tunnelsTo, int estCostToGoal) {
        this.id = id;
        this.flowRate = flowRate;
        this.tunnelsTo = Arrays.copyOf(tunnelsTo, tunnelsTo.length);
    }

    @Override
    public int compareTo(Day16Valve that) {
        return Integer.compare(this.flowRate, that.flowRate);
    }

    @Override
    public String toString() {
        return id + "=" + flowRate;
    }
}
