package bob.algorithm;

import bob.data.graph.Node;

/**
 * A graph node to be used in the A* search algorithm.
 *
 * @param <I> The type of node IDs
 */
public interface AStarNode<I> extends Node<I> {

    /**
     * Retrieve the cost from the starting point to this node.
     *
     * @return The cost to this node
     */
    int getCostToHere();

    /**
     * Retrieve the estimated cost from this node to the finish point.
     *
     * @return The cost to the finish
     */
    int getEstCostToGoal();

    /**
     * Determine if the node has already had its neighbors added to the paths to check.
     *
     * @return The visited status of the node
     */
    boolean isVisited();

    /**
     * Set the cost from the starting point to this node.
     *
     * @param cost The cost to this node
     */
    void setCostToHere(int cost);

    /**
     * Set the path used to get to this node.
     *
     * @param from The node originating the edge being traversed
     */
    default void setPathToHere(AStarNode<I> from) {
    }

    /**
     * Set the visited status of the node.
     *
     * @param visited The visited status of the node
     */
    void setVisited(boolean visited);

    /**
     * Visit the node before processing the edges leaving the node. This allows edges and nodes to be built dynamically
     * as the algorithm is processed.
     */
    default void visit() {
    }
}
