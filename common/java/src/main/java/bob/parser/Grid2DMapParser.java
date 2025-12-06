package bob.parser;

import bob.data.grid.Grid2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Parse input grid data with one row on each line. Each part starts with an identifier line.
 *
 * @param <T> The type of object in each point in the grid
 */
public class Grid2DMapParser<T> implements PuzzleDataParser<Map<Integer, Grid2D<T>>> {

    private final boolean wrapX;
    private final boolean wrapY;
    private final Function<Character, T> translator;
    private final Function<String, Integer> idExtractor;
    private Integer currentID;
    private Grid2D<T> currentGrid;
    private final Map<Integer, Grid2D<T>> result = new HashMap<>();

    public Grid2DMapParser(boolean wrapX, boolean wrapY, Function<Character, T> translator,
            Function<String, Integer> idExtractor) {
        this.wrapX = wrapX;
        this.wrapY = wrapY;
        this.translator = translator;
        this.idExtractor = idExtractor;
    }

    @Override
    public void open(int partnum) {
        currentID = null;
        currentGrid = new Grid2D<>(wrapX, wrapY);
    }

    @Override
    public void read(int partnum, String line) {
        if (currentID == null) {
            currentID = idExtractor.apply(line);
        } else {
            List<T> row = line.chars()
                    .mapToObj(i -> translator.apply((char) i))
                    .collect(Collectors.toList());
            currentGrid.addRow(row);
        }
    }

    @Override
    public void close(int partnum) {
        result.put(currentID, currentGrid);
    }

    @Override
    public Map<Integer, Grid2D<T>> getResult() {
        return result;
    }
}
