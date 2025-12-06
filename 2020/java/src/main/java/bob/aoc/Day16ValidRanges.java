package bob.aoc;

import bob.util.Assert;
import java.util.NavigableSet;
import java.util.TreeSet;
import lombok.Getter;

@Getter
public class Day16ValidRanges {

    private final String fieldname;
    private final int lo1;
    private final int hi1;
    private final int lo2;
    private final int hi2;
    private final NavigableSet<Integer> possibleFieldNums = new TreeSet<>();

    public Day16ValidRanges(String line) {
        int pos = line.indexOf(": ");
        Assert.that((pos > 1), "no field name found in rule: " + line);
        fieldname = line.substring(0, pos);
        int pos1 = line.indexOf("-", (pos + 2));
        lo1 = Integer.parseInt(line.substring((pos + 2), pos1));
        int pos2 = line.indexOf(" or ", pos1 + 1);
        hi1 = Integer.parseInt(line.substring(pos1 + 1, pos2));
        pos1 = line.indexOf("-", pos2 + 4);
        lo2 = Integer.parseInt(line.substring(pos2 + 4, pos1));
        hi2 = Integer.parseInt(line.substring(pos1 + 1));
    }

    public boolean isValid(int value) {
        return ((value >= lo1) && (value <= hi1)) || ((value >= lo2) && (value <= hi2));
    }

    // Add any field positions where the ticket has a value that fits this field's ranges
    public void addPossibles(int[] ticketValues) {
        for (int i = 0; i < ticketValues.length; i++) {
            if (isValid(ticketValues[i])) {
                possibleFieldNums.add(i);
            }
        }
    }

    // Remove any field positions where the ticket has a value that doesn't fit this field's ranges
    public void removePossibles(int[] ticketValues) {
        for (int i = 0; i < ticketValues.length; i++) {
            if (!isValid(ticketValues[i])) {
                possibleFieldNums.remove(i);
            }
        }
    }

    @Override
    public String toString() {
        return fieldname + ":[" + lo1 + "-" + hi1 + "],[" + lo2 + "-" + hi2 + "]";
    }
}
