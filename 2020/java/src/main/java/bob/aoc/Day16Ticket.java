package bob.aoc;

import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Day16Ticket {

    private int[] values;
    @Setter
    private boolean valid;

    public Day16Ticket(String line) {
        if (!line.contains("ticket")) {
            String[] bits = line.split(",");
            values = new int[bits.length];
            for (int i = 0; i < bits.length; i++) {
                values[i] = Integer.parseInt(bits[i]);
            }
            valid = true;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }
}
