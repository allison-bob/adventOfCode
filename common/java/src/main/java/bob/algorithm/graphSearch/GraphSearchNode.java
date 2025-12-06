package bob.algorithm.graphSearch;

import bob.data.graph.Node;
import lombok.Getter;

/**
 * A base class for nodes in a graph to be searched. Depending on the algorithm being used, some of these fields may
 * not be used.
 * 
 * @param <I> The type of node ID
 */
@Getter
public class GraphSearchNode<I> implements Node<I> {

    private final I id;

    /**
     * Normal constructor.
     * 
     * @param id The ID of the graph node
     */
    public GraphSearchNode(I id) {
        this.id = id;
    }
}
