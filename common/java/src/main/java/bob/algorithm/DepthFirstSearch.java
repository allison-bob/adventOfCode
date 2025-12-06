package bob.algorithm;

import bob.data.graph.Graph;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an implementation of the depth-first search (DFS) algorithm as described in
 * {@linkplain https://en.wikipedia.org/wiki/Depth-first_search Wikipedia}.
 * <p>
 * Depth-first search (DFS) is an algorithm for traversing or searching tree or graph data structures. The algorithm
 * starts at the root node (selecting some arbitrary node as the root node in the case of a graph) and explores as far
 * as possible along each branch before backtracking. Extra memory, usually a stack, is needed to keep track of the
 * nodes discovered so far along a specified branch which helps in backtracking of the graph.
 * </p>
 * <p>
 * This implementation performs a recursive search with the following optimizations:
 * </p>
 * <ul>
 * <li>A cache is retained of all states that are visited to detect duplicated states</li>
 * <li>If a state cannot produce a better outcome than the best outcome so far, the state is not processed</li>
 * </ul>
 * <p>
 * The second optimization implies that each node's estimated cost to goal should best case. If looking for minimum
 * cost, the estimate should be at or below the minimum possible cost; if looking for maximum, the estimate should be at
 * or above the maximum possible cost.
 * </p>
 * <p>
 * Graph nodes must implement the {@link DepthFirstSearchNode} interface.
 * </p>
 *
 * @param <I> The type of node ID
 * @param <N> The type of node objects
 */
public class DepthFirstSearch<I, N extends DepthFirstSearchNode<I>> {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    // The graph being traversed
    protected final Graph<I, N> graph;
    // The cache of states that have been worked out
    protected final Set<N> cache = new HashSet<>();
    // The method to visit a node, either in the node (Node::method()) or in the solver (this::visitnode(node))
    protected Consumer<N> nodeVisitor;
    // A method to determine if the path has reached the goal
    protected Predicate<N> isDone;
    // The method to compare paths, should be either {@code Math::max} or {@code Math::min}
    protected BiFunction<Integer, Integer, Integer> pathCompare;
    // The node and final cost for the best path found so far
    protected int bestCost;
    protected N bestNode;

    public DepthFirstSearch(Graph<I, N> graph, Consumer<N> nodeVisitor, Predicate<N> isDone) {
        this.graph = graph;
        this.nodeVisitor = nodeVisitor;
        this.isDone = isDone;
    }

    /**
     * Find the highest cost route.
     *
     * @param initNode The initial node to search from
     * @return The maximum cost
     */
    public N findMaxCost(N initNode) {
        pathCompare = Math::max;
        bestCost = Integer.MIN_VALUE;
        bestPath(initNode);
        return bestNode;
    }

    /**
     * Find the lowest cost route.
     *
     * @param initNode The initial node to search from
     * @return The minimum cost
     */
    public N findMinCost(N initNode) {
        pathCompare = Math::min;
        bestCost = Integer.MAX_VALUE;
        bestPath(initNode);
        return bestNode;
    }

    /**
     * Find the best path. Normally only called from one of the find*Cost methods.
     *
     * @param curr The node to search from
     */
    protected void bestPath(N curr) {
        LOG.debug("bestPath: ({}/{}+{}={}): {}", bestCost, curr.getCostToHere(), curr.getEstCostToGoal(),
                (curr.getCostToHere() + curr.getEstCostToGoal()), curr);

        // If this state has been visited, skip this
        if (cache.contains(curr)) {
            return;
        }

        // If this state can't possibly produce a better result, don't continue
        if (pathCompare.apply((curr.getCostToHere() + curr.getEstCostToGoal()), bestCost) == bestCost) {
            return;
        }

        // Add the current state to the cache
        cache.add(curr);

        // If this is a final state, check to see if we improved things
        if (isDone.test(curr)) {
            int newBest = pathCompare.apply(bestCost, curr.getCostToHere());
            if (newBest != bestCost) {
                bestCost = newBest;
                bestNode = curr;
            }
            return;
        } else {
            // We know that the final answer will be at least what this path has so far
            // (the '-1' ensures that the code above saves the result node)
//            bestCost = pathCompare.apply((bestCost - 1), curr.getCostToHere());
        }

        // Visit the node (permits dynamic node generation)
        nodeVisitor.accept(curr);

        // Check each neighbor of this node
        Map<N, Integer> neighbors = graph.getNeighborMap(curr.getId());
        for (N n : neighbors.keySet()) {
            if (!cache.contains(n)) {
                // Neighbor state has not already been processed
                bestPath(n);
            }
        }
    }
}
