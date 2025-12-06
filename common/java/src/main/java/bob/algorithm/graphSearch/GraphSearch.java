package bob.algorithm.graphSearch;

import bob.data.graph.Graph;
import bob.util.Assert;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the base class for searches in graphs using a Breadth-First Search, Depth-First Search, Dijkstra's
 * Algorithm, or A* Algorithm.
 * <p>
 * Much of the understanding of these search algorithms comes from
 * <a href="https://aoc.just2good.co.uk/python/shortest_paths">Dazbo's Advent of Code solutions</a>. Although the code
 * samples are in Python, the site has good descriptions of the algorithms, when to use them, and how to implement them.
 * </p>
 * <p>
 * All of the search algorithms utilize the concept of a frontier. The algorithms start with a specific graph node (the
 * "start" node) in a cache of some sort then repeatedly performing the following process until the completion criteria
 * are met:
 * </p>
 * <ol>
 * <li>Remove the next node from the cache (the "current" node, each algorithm has a different way of deciding which
 * node is next)</li>
 * <li>Expand the frontier by adding all the nodes at the end of each edge emanating from the current node (the
 * "neighbors")</li>
 * </ol>
 * <p>
 * The node visitor is executed after creating the cache object for the node but before any operations about the node.
 * This allows the cache object to be populated with computed data and for nodes and edges to be dynamically created
 * while executing the algorithm.
 * </p>
 * <p>
 * These algorithms include a cache of nodes that have been visited during the search. This ensures that nodes are
 * processed only once. This record should be kept separate from the graph so multiple searches can be run if necessary
 * while finding the puzzle solution.
 * </p>
 * @param <I> The type of node ID
 * @param <N> The type of node objects
 */
@Getter
public class GraphSearch<I, N extends GraphSearchNode<I>> {

    private interface VoidFunction {

        void apply();
    }

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    // The graph being traversed
    private final Graph<I, N> graph;
    // The cache of states that have been worked out
    protected final Map<I, GraphSearchCache<I>> cache = new TreeMap<>();
    // The nodes to be visited, entered as encountered
    protected Deque<GraphSearchCache<I>> queue;
    // The nodes to be visited, sorted by increasing estimated total cost
    protected PriorityQueue<GraphSearchCache<I>> pqueue;
    // The method to push a new entry onto the active queue
    private Consumer<GraphSearchCache<I>> push;
    // The method to retrieve and remove the "next" entry from the active queue
    private Supplier<GraphSearchCache<I>> pop;
    // The method to clear the active queue
    private VoidFunction flush;
    // The method to visit a node, either in the node (Day99Node.method()) or in the solver (Day99a.visitnode(node))
    @Setter
    private Consumer<GraphSearchCache<I>> nodeVisitor;
    // A method to determine if the path has reached the goal
    @Setter
    private Predicate<GraphSearchCache<I>> isDone;
    // A flag to determine if we should re-visit nodes
    @Setter
    private boolean revisit;

    protected GraphSearch(Graph<I, N> graph) {
        this.graph = graph;
    }

    /**
     * Initialize with a FIFO queue. This is used for breadth-first searches.
     */
    protected void initFifoQueue() {
        queue = new ArrayDeque<>();
        push = queue::offerLast;
        pop = queue::pollFirst;
        flush = queue::clear;
    }

    /**
     * Initialize with a LIFO queue. This is used for depth-first searches.
     */
    protected void initLifoQueue() {
        queue = new ArrayDeque<>();
        push = queue::offerFirst;
        pop = queue::pollFirst;
        flush = queue::clear;
    }

    /**
     * Initialize with a priority queue. This is used for the A* and Dijkstra's algorithms.
     * @param findMax {@code true} to find maximum cost, {@code false} to find minimum cost
     */
    protected void initPriorityQueue(boolean findMax) {
        if (findMax) {
            pqueue = new PriorityQueue<>(
                    (a, b) -> Integer.compare(
                            (b.getCostToHere() + b.getEstCostToGoal()),
                            (a.getCostToHere() + a.getEstCostToGoal())
                    )
            );
        } else {
            pqueue = new PriorityQueue<>(
                    (a, b) -> Integer.compare(
                            (a.getCostToHere() + a.getEstCostToGoal()),
                            (b.getCostToHere() + b.getEstCostToGoal())
                    )
            );
        }
        push = pqueue::add;
        pop = pqueue::poll;
        flush = pqueue::clear;
    }

    public GraphSearchCache<I> solve(I start) {
        Assert.that(((queue != null) || (pqueue != null)), "No queue was initialized");
        Assert.that((push != null), "No push method was registered");
        Assert.that((pop != null), "No pop method was registered");
        Assert.that((isDone != null), "No isDone method was registered");

        // Push the starting node onto the queue
        flush.apply();
        cache.clear();
        GraphSearchCache<I> curr = new GraphSearchCache<>(start);
        visit(curr);
        push.accept(curr);

        // Process nodes until we are done or we run out of nodes
        curr = pop.get();
        while (curr != null) {
            //LOG.debug("Processing {}", curr);
            //TreeSet cacheKeys = new TreeSet(cache.keySet());
            //LOG.debug("   Cache contains {}", cacheKeys);
            //LOG.debug("   Queue contains {}", queue);
            //LOG.debug("   PQueue contains {}", pqueue);

            // If this node has been visited, skip this
            if ((!cache.containsKey(curr.getId())) || revisit) {

                // Add the current node to the cache
                cache.put(curr.getId(), curr);

                // Are we done yet?
                if (isDone.test(curr)) {
                    return curr;
                }

                // Add each neighbor to the queue
                Map<N, Integer> neighbors = graph.getNeighborMap(curr.getId());
                //LOG.debug("   neighbors: {}", neighbors);
                for (N neighbor : neighbors.keySet()) {
                    // Only add if the node hasn't been processed
                    if (!cache.containsKey(neighbor.getId())) {
                        GraphSearchCache<I> gscN = new GraphSearchCache<>(neighbor.getId());
                        gscN.setCostToHere(curr.getCostToHere() + neighbors.get(neighbor));
                        gscN.setDepth(curr.getDepth() + 1);
                        gscN.setPathToHere(curr.getId());
                        visit(gscN);
                        push.accept(gscN);
                    }
                }

                curr.setVisited(true);
            }
            //cacheKeys = new TreeSet(cache.keySet());
            //LOG.debug("   Cache now contains {}", cacheKeys);
            //LOG.debug("   Queue now contains {}", queue);

            curr = pop.get();
        }

        // Ran out of nodes to process without isDone being true, so return null
        return null;
    }

    /**
     * Visit the node before processing the edges leaving the node. This allows edges and nodes to be built dynamically
     * as the algorithm is processed.
     * @param gsc The cache object for the node to be visited
     */
    public void visit(GraphSearchCache<I> gsc) {
        if (nodeVisitor != null) {
            nodeVisitor.accept(gsc);
        }
    }
}
