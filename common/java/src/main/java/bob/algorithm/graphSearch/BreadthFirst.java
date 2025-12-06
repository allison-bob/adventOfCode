package bob.algorithm.graphSearch;

import bob.data.graph.Graph;
import bob.util.Assert;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Implementation of a Breadth First Search (BFS).
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
 * <li>The BFS algorithm ensures that the same vertex is not followed twice; i.e. we never revisit a node we’ve visited
 * before. This is crucial because it means that once we’ve a path to a node, we must have found the shortest route to
 * that node.</li>
 * </ul>
 * @param <I> The type of node ID
 * @param <N> The type of node objects
 */
public class BreadthFirst<I, N extends GraphSearchNode<I>> extends GraphSearch<I, N> {

    public BreadthFirst(Graph<I, N> graph) {
        super(graph);
        initFifoQueue();
    }

    /**
     * Find all end nodes that can be reached from the start node.
     * @param start     The ID of the start node
     * @param isEndNode A method to determine if the current node is an end node
     * @return The list of IDs of the end nodes that were reached
     */
    public List<I> findAllEndNodes(I start, Predicate<I> isEndNode) {
        List<I> endNodes = new ArrayList<>();
        setIsDone(gsc -> {
            if (isEndNode.test(gsc.getId())) {
                endNodes.add(gsc.getId());
            }
            return false;
        });

        GraphSearchCache<I> result = solve(start);
        Assert.that((result == null), "A result was returned");

        return endNodes;
    }

    /**  --  DELETE  --
     * Find the number of distinct routes to all end nodes that can be reached from the start node.
     * @param start     The ID of the start node
     * @param isEndNode A method to determine if the current node is an end node
     * @return The number of distinct routes
     */
    public int findAllRoutesCount(I start, Predicate<I> isEndNode) {
        // Using an int variable fails because it can't be incremented in the lambda
        List<Integer> counter = new ArrayList<>();
        setIsDone(gsc -> {
            if (isEndNode.test(gsc.getId())) {
                counter.add(1);
            }
            return false;
        });

        setRevisit(true);
        GraphSearchCache<I> result = solve(start);
        Assert.that((result == null), "A result was returned");

        return counter.size();
    }

    /**  --  DELETE  --
     * Find the number of distinct routes to all end nodes that can be reached from the start node.
     * @param start     The ID of the start node
     * @param isEndNode A method to determine if the current node is an end node
     * @return The number of distinct routes
     */
    public Set<I> findAllRoutesNodes(I start, Predicate<I> isEndNode) {
        Set<I> nodeList = new HashSet<>();
//        setIsDone(gsc -> {
//            if (isEndNode.test(gsc.getId())) {
//                Deque<I> toCheck = new ArrayDeque<>(gsc.getPathToHere());
//                while (!toCheck.isEmpty()) {
//                    I nxt = toCheck.pop();
//                    if (!nodeList.contains(nxt)) {
//                        nodeList.add(nxt);
//                        GraphSearchCache<I> gscnxt = cache.get(nxt);
//                        toCheck.addAll(gscnxt.getPathToHere());
//                    }
//                }
//            }
//            return false;
//        });
//
//        setRevisit(true);
//        GraphSearchCache<I> result = solve(start);
//        Assert.that((result == null), "A result was returned");

        return nodeList;
    }

    /**
     * Find the number of nodes connected to the start node.
     * @param start The ID of the start node
     * @return The node count
     */
    public int findConnectedNodeCount(I start) {
        setIsDone(gsc -> false);

        GraphSearchCache<I> result = solve(start);
        Assert.that((result == null), "A result was returned");

        return cache.size();
    }

    /**
     * Find the IDs of nodes connected to the start node.
     * @param start The ID of the start node
     * @return The node IDs
     */
    public Set<I> findConnectedNodes(I start) {
        setIsDone(gsc -> false);

        GraphSearchCache<I> result = solve(start);
        Assert.that((result == null), "A result was returned");

        return cache.keySet();
    }

    /**
     * Find the length of the shortest path from the start node to the end node.
     * @param start The ID of the start node
     * @param end   The ID of the end node
     * @return The length of the shortest path
     */
    public int findShortestPathLength(I start, I end) {
        setIsDone(gsc -> gsc.getId().equals(end));

        GraphSearchCache<I> result = solve(start);
        Assert.that((result != null), "No result returned");

        return result.getDepth();
    }
}
