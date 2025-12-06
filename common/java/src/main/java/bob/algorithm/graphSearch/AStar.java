package bob.algorithm.graphSearch;

import bob.data.graph.Graph;
import bob.util.Assert;

/**
 * Implementation of A* Algorithm.
 * <p>
 * The A* (pronounced "A star") Algorithm is a lot like BFS. But instead of expanding frontier in all directions
 * equally, we prioritize popping from the frontier in the direction that we believe takes us closer to our goal. To do
 * this, we need two key changes, compared to the BFS:
 * </p>
 * <ul>
 * <li>We need to use a priority queue. Recall that a priority queue allows us to pop elements based on a priority.</li>
 * <li>We need to provide a heuristic function, i.e. a function that computes some sort of cost. The heuristic estimates
 * the cost to get from any given node to the goal. This cost is used to determine the priority. Typically, the
 * heuristic will be a function that estimates distance, such as a Manhattan distance function.</li>
 * </ul>
 * @param <I> The type of node ID
 * @param <N> The type of node objects
 */
public class AStar<I, N extends GraphSearchNode<I>> extends GraphSearch<I, N> {

    public AStar(Graph<I, N> graph) {
        super(graph);
        initPriorityQueue(false);
    }

    /**
     * Find the lowest cost route. Note that a node visitor will need to be registered before calling this method to set
     * the estimated cost to goal for the node being visited.
     * @param start The ID of the start node
     * @param end   The ID of the end node
     * @return The minimum cost
     */
    public int findMinCost(I start, I end) {
        initPriorityQueue(false);
        setIsDone(gsc -> gsc.getId().equals(end));

        GraphSearchCache<I> result = solve(start);
        Assert.that((result != null), "No result returned");

        return result.getCostToHere();
    }
}
