package bob.parser;

import bob.util.Assert;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Parse input data with an identifier line and one or more data lines in each part.
 *
 * @param <K> The type of key to create
 * @param <V> The type of object to create, must include the key
 */
public class PartObjectMapParser<K, V> implements PuzzleDataParser<Map<K, List<V>>> {

    Function<String, V> translator;
    Function<String, K> idExtractor;
    private K currentID;
    private List<V> currentList;

    private final Map<K, List<V>> entries = new HashMap<>();

    public PartObjectMapParser(Function<String, V> translator, Function<String, K> idExtractor) {
        this.translator = translator;
        this.idExtractor = idExtractor;
    }

    @Override
    public void open(int partnum) {
        currentID = null;
        currentList = new ArrayList<>();
    }

    @Override
    public void read(int partnum, String line) {
        if (currentID == null) {
            currentID = idExtractor.apply(line);
        } else {
            currentList.add(translator.apply(line));
        }
    }

    @Override
    public void close(int partnum) {
        if (currentID != null) {
            entries.put(currentID, currentList);
        }
    }

    @Override
    public Map<K, List<V>> getResult() {
        return entries;
    }
}
