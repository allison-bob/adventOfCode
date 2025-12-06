package bob.data.graph;

/**
 * Basic contract for nodes in a graph.
 *
 * @param <I> The type of node ID
 */
public interface Node<I> {

    I getId();
}
