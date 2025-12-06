package bob.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse input data consisting of a comma-separated list of integers on each line and all data in a single part. If the
 * part consists of multiple lines, the numbers on all lines are added to the list.
 */
public class IntegerListParser implements PuzzleDataParser<List<Integer>> {

    private final List<Integer> entries = new ArrayList<>();

    @Override
    public void read(int partnum, String line) {
        String[] values = line.split(",");
        for (String v : values) {
            if (!v.isBlank()) {
                entries.add(Integer.valueOf(v.trim()));
            }
        }
    }

    @Override
    public List<Integer> getResult() {
        return entries;
    }
}
