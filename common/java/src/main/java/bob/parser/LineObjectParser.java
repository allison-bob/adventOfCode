package bob.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Parse input data with one entry on each line, converting each character to an object, and all data in a single part.
 *
 * @param <T> The type of object to create for each character of the input line
 */
public class LineObjectParser<T> implements PuzzleDataParser<List<List<T>>> {

    private final Function<Character, T> translator;
    private List<List<T>> result = new ArrayList<>();

    public LineObjectParser(Function<Character, T> translator) {
        this.translator = translator;
    }

    @Override
    public void read(int partnum, String line) {
        List<T> row = line.chars()
                .mapToObj(i -> translator.apply((char) i))
                .collect(Collectors.toList());
        result.add(row);
    }

    @Override
    public List<List<T>> getResult() {
        return result;
    }
}
