package bob.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse input data consisting of a single integer on each line and all data in a single part.
 */
public class SingleIntegerParser implements PuzzleDataParser<List<Integer>> {

    private final List<Integer> entries = new ArrayList<>();

    @Override
    public void read(int partnum, String line) {
        entries.add(Integer.valueOf(line));
    }

    @Override
    public List<Integer> getResult() {
        return entries;
    }
}
