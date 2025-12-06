package bob.algorithm.graphSearch;

import bob.data.graph.Graph;
import bob.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;

/**
 * Implementation of a Depth First Search (DFS).
 * <p>
 * BFS can be used to find a path (or all paths) from a to b. It can also be used to find paths from a to all locations
 * in the graph. It is guaranteed to find a solution if one exists. The frontier expands at an equal rate in all
 * directions, and uses a FIFO (first-in, first-out) queue to implement the frontier.
 * </p>
 * <p>
 * How it works:
 * </p>
 * <ul>
 * <li>We want to explore a graph, made up of edges connected by vertices. E.g. if we’re solving the path through a map,
 * then each map location is a vertex. The graph has a start location, which is the root node of our tree. So we add the
 * root to the frontier.</li>
 * <li>It then explores all adjacent nodes. These will be distance=1 from the root. We add those to the frontier.</li>
 * <li>Then we explore all the nodes that are adjacent to those nodes. I.e. the nodes that are distance=2 from the
 * root.</li>
 * <li>The DFS algorithm ensures that the same vertex is not followed twice; i.e. we never revisit a node we’ve visited
 * before. This is crucial because it means that once we’ve a path to a node, we must have found the shortest route to
 * that node.</li>
 * </ul>
 * @param <I> The type of node ID
 * @param <N> The type of node objects
 */
public class DepthFirst<I, N extends GraphSearchNode<I>> extends GraphSearch<I, N> {

    // Path of current route
    private List<I> path;

    /**
     * Normal constructor.
     * @param graph The graph to traverse
     */
    public DepthFirst(Graph<I, N> graph) {
        super(graph);
        initLifoQueue();
    }

    private void backtrackCheck(GraphSearchCache<I> gsc) {
        //LOG.debug("      check {} -- {}:{}", gsc, path.size(), path);
        while (gsc.getDepth() < path.size()) {
            //LOG.debug("      ... backtracking");
            I endID = path.remove(path.size() - 1);
            cache.remove(endID);
        }
        path.add(gsc.getId());
    }

    /**
     * Find the number of distinct routes to all end nodes that can be reached from the start node.
     * @param start     The ID of the start node
     * @param isEndNode A method to determine if the current node is an end node
     * @return The number of distinct routes
     */
    public int findAllRoutesCount(I start, Predicate<I> isEndNode) {
        // Using an int variable fails because it can't be incremented in the lambda
        List<Integer> counter = new ArrayList<>();
        path = new ArrayList<>();
        setIsDone(gsc -> {
            backtrackCheck(gsc);
            if (isEndNode.test(gsc.getId())) {
                counter.add(1);
            }
            return false;
        });

        GraphSearchCache<I> result = solve(start);
        Assert.that((result == null), "A result was returned");

        return counter.size();
    }

    /**
     * Find the number of distinct lowest-cost routes from the start node to the end node.
     * @param start The ID of the start node
     * @param end   The ID of the end node
     * @return A map entry: key = total cost, value = route count
     */
    public Map.Entry<Integer, Integer> findBestRoutesCount(I start, I end) {
        // Map of cost to route count
        TreeMap<Integer, Integer> counter = new TreeMap<>();
        path = new ArrayList<>();
        setIsDone(gsc -> {
            backtrackCheck(gsc);
            if (gsc.getId().equals(end)) {
                Integer count = counter.get(gsc.getCostToHere());
                if (count == null) {
                    count = 0;
                }
                count++;
                counter.put(gsc.getCostToHere(), count);
            }
            return false;
        });

        GraphSearchCache<I> result = solve(start);
        Assert.that((result == null), "A result was returned");
        LOG.debug("Counters: {}", counter);

        return counter.firstEntry();
    }

    /**
     * Find the number of distinct routes to all end nodes that can be reached from the start node.
     * @param start     The ID of the start node
     * @param isEndNode A method to determine if the current node is an end node
     * @return The number of distinct routes
     */
    public Set<I> findAllRoutesNodes(I start, Predicate<I> isEndNode) {
        return null;
    }
}
