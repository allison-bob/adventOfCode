package bob.parser;

import bob.data.grid.Grid2D;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Parse input grid data with one row on each line in a single part.
 *
 * @param <T> The type of object in each point in the grid
 */
public class Grid2DParser<T> implements PuzzleDataParser<Grid2D<T>> {

    private final boolean wrapX;
    private final boolean wrapY;
    private final Function<Character, T> translator;
    private Grid2D<T> result;

    public Grid2DParser(boolean wrapX, boolean wrapY, Function<Character, T> translator) {
        this.wrapX = wrapX;
        this.wrapY = wrapY;
        this.translator = translator;
    }

    @Override
    public void open(int partnum) {
        result = new Grid2D<>(wrapX, wrapY);
    }

    @Override
    public void read(int partnum, String line) {
        List<T> row = line.chars()
                .mapToObj(i -> translator.apply((char) i))
                .collect(Collectors.toList());
        result.addRow(row);
    }

    @Override
    public Grid2D<T> getResult() {
        return result;
    }
}
