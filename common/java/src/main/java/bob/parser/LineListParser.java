package bob.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Parse input data with one entry on each line, splitting each line on the specified delimiter and converting each
 * non-empty segment to an object, and all data in a single part.
 *
 * @param <T> The type of object to create for each segment of the input line
 */
public class LineListParser<T> implements PuzzleDataParser<List<List<T>>> {

    private final String delimiter;
    private final Function<String, T> translator;
    private List<List<T>> result = new ArrayList<>();

    public LineListParser(String delimiter, Function<String, T> translator) {
        this.delimiter = delimiter;
        this.translator = translator;
    }

    @Override
    public void read(int partnum, String line) {
        String[] bits = line.split(delimiter);
        List<T> row = Stream.of(bits)
                .filter(s -> !s.isBlank())
                .map(translator)
                .collect(Collectors.toList());
        result.add(row);
    }

    @Override
    public List<List<T>> getResult() {
        return result;
    }
}
