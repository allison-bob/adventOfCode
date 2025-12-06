package bob.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse input data consisting of a single long on each line and all data in a single part.
 */
public class SingleLongParser implements PuzzleDataParser<List<Long>> {

    private final List<Long> entries = new ArrayList<>();

    @Override
    public void read(int partnum, String line) {
        entries.add(Long.valueOf(line));
    }

    @Override
    public List<Long> getResult() {
        return entries;
    }
}
