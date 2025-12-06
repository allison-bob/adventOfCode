package bob.algorithm;

import bob.data.graph.Graph;
import bob.util.Assert;
import java.util.PriorityQueue;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an implementation of the A* search algorithm as described in
 * {@linkplain https://en.wikipedia.org/wiki/A*_search_algorithm Wikipedia}.
 * <p>
 * A* (pronounced "A-star") is a graph traversal and path search algorithm, which is used in many fields of computer
 * science due to its completeness, optimality, and optimal efficiency.
 * </p>
 * <p>
 * A* is an informed search algorithm, or a best-first search, meaning that it is formulated in terms of weighted
 * graphs: starting from a specific starting node of a graph, it aims to find a path to the given goal node having the
 * smallest cost (least distance traveled, shortest time, etc.). It does this by maintaining a tree of paths originating
 * at the start node and extending those paths one edge at a time until its termination criterion is satisfied.
 * </p>
 * <p>
 * Graph nodes must implement the {@link AStarNode} interface.
 * </p>
 *
 * @param <I> The type of node ID
 * @param <N> The type of node objects
 */
public class AStar<I, N extends AStarNode<I>> {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    // The graph being traversed
    protected final Graph<I, N> graph;
    // The nodes to be visited, sorted by increasing estimated total cost
    protected final PriorityQueue<N> open;

    public AStar(Graph<I, N> graph) {
        this.graph = graph;
        this.open = new PriorityQueue<>(
                (a, b) -> Integer.compare(
                        (a.getCostToHere() + a.getEstCostToGoal()),
                        (b.getCostToHere() + b.getEstCostToGoal())
                )
        );
    }

    public int findPath(I start, I finish) {
        Assert.that((start != null), "start is null");
        Assert.that((finish != null), "finish is null");
        return findPathImpl(start, finish, null);
    }

    public int findPath(I start, Predicate<N> isDone) {
        Assert.that((start != null), "start is null");
        Assert.that((isDone != null), "isDone is null");
        return findPathImpl(start, null, isDone);
    }

    private int findPathImpl(I start, I finish, Predicate<N> isDone) {
        // Set the cost-to-here of the starting node to 0
        graph.get(start).setCostToHere(0);

        // Add the starting node as the initial path to be checked
        open.add(graph.get(start));

        // Visit nodes until we run out or the lowest cost node is the finish
        int i = 0;
        while (!open.isEmpty()) {
            N node = open.poll();
//            if ((i % 1000) == 0) {
//                LOG.debug("({},{}) Visiting [{}]\n{}",
//                        i, open.size(), (node.getCostToHere() + node.getEstCostToGoal()), node);
//            }
            if (isDone != null) {
                if (isDone.test(node)) {
                    break;
                }
            } else {
                if (node.getId().equals(finish)) {
                    break;
                }
            }
            visitNode(node);
            i++;
        }
        LOG.debug("Finished after {} visits with {} nodes still open", i, open.size());
        LOG.debug(graph.toString());

        return graph.get(finish).getCostToHere();
    }

    protected void visitNode(N node) {
        if (node.isVisited()) {
            return;
        }

        // Visit the node
        node.visit();

        // Check each neighbor of this node
        LOG.debug("Checking neighbors of {}:", node.getId());
        graph.getNeighborMap(node.getId()).forEach((n, c) -> {
            checkNeighbor(node, n, (c + node.getCostToHere()));
        });
        node.setVisited(true);
    }

    protected void checkNeighbor(N node, N neighbor, int cost) {
        // Make sure the node hasn't already been visited
        LOG.debug("   Checking neighbor {}: {} -  visited {}", neighbor.getId(), cost, neighbor.isVisited());
        if (neighbor.isVisited()) {
            return;
        }

        // Update the tentative cost to this node
        if (cost < neighbor.getCostToHere()) {
            //LOG.debug("      Update cost to {}", cost);
            neighbor.setCostToHere(cost);
            neighbor.setPathToHere(node);
        } else {
            //LOG.debug("      Current cost ({}) is lower", node.getCostToHere());
        }

        // Add this node to the list to check
        open.add(neighbor);
    }
}
