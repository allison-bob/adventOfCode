package bob.algorithm;

import bob.data.graph.Graph;
import bob.data.graph.Node;
import bob.util.Assert;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an implementation of the Floyd–Warshall algorithm as described in
 * {@linkplain https://en.wikipedia.org/wiki/Floyd–Warshall_algorithm Wikipedia}.
 * <p>
 * This algorithm effectively searches all edges of a graph to find all possible paths between nodes, looking for the
 * maximum or minimum total cost for the path. This implementation assumes a single starting point but makes no
 * assumption about a specific ending point.
 * </p>
 * <p>
 * The heart of the algorithm is a function {@code shortestPath(i,j,k)} which defines the cost of the best path between
 * nodes {@code i} and {@code j} optionally going through node {@code k}. If the nodes are labeled 1..N, the function
 * can be defined as follows:
 * </p>
 * <ul>
 * <li>If k=0, return the cost of the edge from i to j if it exists, otherwise return infinity</li>
 * <li>If k!=0, return the maximum of
 * <ul>
 * <li>{@code shortestPath(i,j,k-1)} (a path that does not go through k)</li>
 * <li>The sum of {@code shortestPath(i,k,k-1)} and {@code shortestPath(k,j,k-1)} (the two parts of a path going
 * through k)</li>
 * </ul>
 * </ul>
 *
 * @param <I> The type of node ID
 * @param <N> The type of node objects
 * @param <C> The type of cache objects
 */
public class FloydWarshall<I, N extends Node<I>, C extends FloydWarshallState<I>> {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    // The graph being traversed
    protected final Graph<I, N> graph;
    // The method to compare paths, should be either {@code Math::max} or {@code Math::min}
    protected BiFunction<Integer, Integer, Integer> pathCompare;
    // Initial value for evaluating the best route
    protected int worstRoute;
    // A method to construct a new state for transitioning to a new node
    private final BiFunction<C, N, C> stateConstructor;
    // The cache of states that have been worked out
    private final Map<C, Integer> cache = new HashMap<>();

    /**
     * Normal constructor.
     * @param graph The graph to traverse
     * @param stateConstructor A method to construct a new state to transition to another node
     */
    public FloydWarshall(Graph<I, N> graph, BiFunction<C, N, C> stateConstructor) {
        this.graph = graph;
        this.stateConstructor = stateConstructor;
    }

    /**
     * Find the highest cost route.
     * @param initState The initial state to search from
     * @return The maximum cost
     */
    public int findMaxCost(C initState) {
        pathCompare = Math::max;
        worstRoute = Integer.MIN_VALUE;
        return bestPath(initState);
    }

    /**
     * Find the lowest cost route.
     * @param initState The initial state to search from
     * @return The minimum cost
     */
    public int findMinCost(C initState) {
        pathCompare = Math::min;
        worstRoute = Integer.MAX_VALUE;
        return bestPath(initState);
    }

    /**
     * Start a new search related to the current search. This is used by subclasses to add additional search streams.
     * @param initState The initial state to search from
     * @return The optimal cost
     */
    protected int findCost(C initState) {
        Assert.that((pathCompare != null), "path comparator is null");
        return bestPath(initState);
    }
    
    /**
     * Find the best path. Normally only called from one of the find*Cost methods.
     * @param state The state to search from
     * @return The optimal cost
     */
    protected int bestPath(C state) {
        // If the current state has already been processed, just return that value
        if (cache.keySet().contains(state)) {
            return cache.get(state);
        }

        // Check to see if processing on this path is complete
        Integer done = isDone(state);
        if (done != null) {
            return done;
        }
        
        // Get the actual cost to here
        int result = visit(state);

        // Find the best route from here to the end
        for (Map.Entry<N, Integer> e : graph.getNeighborMap(state.getCurrentNode()).entrySet()) {
            result = pathCompare.apply(result, findCost(stateConstructor.apply(state, e.getKey()))) + e.getValue();
        }
        
        // Cache this result
        cache.put(state, result);

        return result;
    }
    
    /**
     * Override this method to indicate that a path should not be followed. If this method returns a non-null
     * value, that value will be used as the cost of the best path.
     * @param state The state to examine
     * @return {@code null} to continue or a result to return
     */
    protected Integer isDone(C state) {
        return null;
    }
    
    /**
     * Visit the current node. This method allows branching to occur based on puzzle-specific conditions.
     * @param state The state to examine
     * @return The best result for this node
     */
    protected int visit(C state) {
        return worstRoute;
    }
}
