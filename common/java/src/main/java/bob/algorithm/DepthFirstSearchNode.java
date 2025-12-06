package bob.algorithm;

import bob.data.graph.Node;

/**
 * A graph node to be used in the depth-first search algorithm.
 * <p>
 * If nodes are being generated dynamically, the node should override the standard {@code equals} method to allow proper
 * determination if a state has been visited previously.
 * </p>
 *
 * @param <I> The type of node IDs
 */
public interface DepthFirstSearchNode<I> extends Node<I> {

    /**
     * Retrieve the cost from the starting point to this node.
     *
     * @return The cost to this node
     */
    int getCostToHere();

    /**
     * Retrieve the search depth of this node.
     *
     * @return The search depth
     */
    int getDepth();

    /**
     * Retrieve the estimated cost from this node to the goal.
     *
     * @return The cost to the finish
     */
    int getEstCostToGoal();
}
