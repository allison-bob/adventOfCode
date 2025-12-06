package bob.parser;

import bob.util.Assert;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Parse input data with one entry including an identifier on each line and all data in a single part.
 *
 * @param <K> The type of key to create
 * @param <V> The type of object to create, must include the key
 */
public class LineObjectMapParser<K, V> implements PuzzleDataParser<Map<K, V>> {

    Function<String, V> translator;
    Function<V, K> idExtractor;

    private final Map<K, V> entries = new HashMap<>();

    public LineObjectMapParser(Function<String, V> translator, Function<V, K> idExtractor) {
        this.translator = translator;
        this.idExtractor = idExtractor;
    }

    @Override
    public void read(int partnum, String line) {
        V entry = translator.apply(line);
        K key = idExtractor.apply(entry);
        if (entries.containsKey(key)) {
            throw Assert.failed(null, "duplicate key: " + key);
        }
        entries.put(key, entry);
    }

    @Override
    public Map<K, V> getResult() {
        return entries;
    }
}
