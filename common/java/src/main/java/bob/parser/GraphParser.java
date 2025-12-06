package bob.parser;

import bob.data.graph.Graph;
import bob.data.graph.Node;
import java.util.function.BiConsumer;

/**
 * Parse input graph data with one instruction on each line in a single part. The translator should parse the input
 * line and call methods on the graph to add data.
 *
 * @param <I> The type of node ID
 * @param <N> The type of node objects
 */
public class GraphParser<I, N extends Node<I>> implements PuzzleDataParser<Graph<I, N>> {

    private final BiConsumer<Graph<I, N>, String> translator;
    private Graph<I, N> result;

    public GraphParser(Class<I> idClass, Class<N> nodeClass, BiConsumer<Graph<I, N>, String> translator) {
        this.translator = translator;
        this.result = new Graph<>(idClass, nodeClass);
    }

    @Override
    public void read(int partnum, String line) {
        translator.accept(result, line);
    }

    @Override
    public Graph<I, N> getResult() {
        return result;
    }
}
