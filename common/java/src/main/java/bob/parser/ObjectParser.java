package bob.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Parse input data with one entry on each line and all data in a single part.
 *
 * @param <T> The type of object to create
 */
public class ObjectParser<T> implements PuzzleDataParser<List<T>> {

    Function<String, T> translator;

    private final List<T> entries = new ArrayList<>();

    public ObjectParser(Function<String, T> translator) {
        this.translator = translator;
    }

    @Override
    public void read(int partnum, String line) {
        entries.add(translator.apply(line));
    }

    @Override
    public List<T> getResult() {
        return entries;
    }
}
