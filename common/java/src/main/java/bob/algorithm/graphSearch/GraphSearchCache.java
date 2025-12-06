package bob.algorithm.graphSearch;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * A base class for caching the state of nodes in a graph to be searched. Depending on the algorithm being used, some of
 * these fields may not be used.
 *
 * @param <I> The type of node ID
 */
@Getter
@Setter
public class GraphSearchCache<I> {

    /**
     * The ID of the graph node.
     */
    private final I id;

    /**
     * The cost from the starting point to this node.
     */
    private int costToHere;

    /**
     * Retrieve the path length from the starting point to this node.
     */
    private int depth;

    /**
     * Retrieve the estimated cost from this node to the finish point.
     */
    private int estCostToGoal;

    /**
     * The ID of the node used to get to this node. This field allows back-tracking the complete
     * path to the starting point.
     */
    private I pathToHere;

    /**
     * The visited status of the node.
     */
    private boolean visited;

    /**
     * Normal constructor.
     * @param id The ID of the graph node
     */
    public GraphSearchCache(I id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "[" + id + ":" + depth + ":" + costToHere + "+" + estCostToGoal + "=" +
                (costToHere + estCostToGoal) + "<" + pathToHere + ":" + visited + "]";
    }
}
