package bob.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Parse input data with one entry spanning each input part.
 *
 * @param <T> The type of object to create
 */
public class ObjectListParser<T> implements PuzzleDataParser<List<T>> {

    Supplier<T> constructor;
    BiConsumer<T, String> translator;

    private final List<T> entries = new ArrayList<>();
    private T currentObj;
    private PuzzleDataParser<T> parser;

    public ObjectListParser(Supplier<T> constructor, BiConsumer<T, String> translator) {
        this.constructor = constructor;
        this.translator = translator;
    }

    public ObjectListParser(PuzzleDataParser<T> parser) {
        this.parser = parser;
    }

    @Override
    public void open(int partnum) {
        if (parser != null) {
            parser.open(partnum);
        } else {
            currentObj = constructor.get();
        }
    }

    @Override
    public void read(int partnum, String line) {
        if (parser != null) {
            parser.read(partnum, line);
        } else {
            translator.accept(currentObj, line);
        }
    }

    @Override
    public void close(int partnum) {
        if (parser != null) {
            parser.close(partnum);
            entries.add(parser.getResult());
        } else {
            entries.add(currentObj);
        }
    }

    @Override
    public List<T> getResult() {
        return entries;
    }
}
