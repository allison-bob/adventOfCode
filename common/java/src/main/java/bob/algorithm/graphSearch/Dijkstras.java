package bob.algorithm.graphSearch;

import bob.data.graph.Graph;
import bob.util.Assert;

/**
 * Implementation of Dijkstra’s Algorithm.
 * <p>
 * This algorithm is like BFS. But instead of expanding the frontier in all directions with equal weight, we instead
 * prioritize popping the node that has the lowest overall cost. Dijkstra’s algorithm is perfect for:
 * </p>
 * <ul>
 * <li>Finding the best path through a graph, when the edges of the graph have different weights.</li>
 * <li>Finding the shortest path to every node for any given node.</li>
 * </ul>
 * <p>
 * How it works:
 * </p>
 * <ul>
 * <li>The frontier is implemented using a priority queue. The data structure allows us to efficiently pop the item with
 * the lowest priority.</li>
 * <li>Our starting node will have a cumulative cost of 0.</li>
 * <li>For each valid adjacent node, we store this node along with the cumulative cost so far. This is given by the
 * previous cost, plus the cost of this particular edge. This differs from BFS, since in BFS the cost is always constant
 * for every adjacent node.</li>
 * <li>In BFS, we check for duplicates when we insert. But in Dijkstra’s, if we arrive at a node we’ve arrived at
 * before, then we can skip it if the current cumulative cost is higher than the previous cost to reach this node. I.e.
 * for each node, always store the minimum cost to reach it.</li>
 * <li>In BFS, we typically end when we reach the goal. I.e. because if we’ve reached the goal, we must have taken the
 * minimum number of steps to get to it. But in Dijkstra’s algorithm, there may be different ways to reach the goal, and
 * they may have different costs. So we only end when we pop the goal off the priority queue. Because once we pop the
 * goal, then we know we’ve popped it with the minimum possible cumulative cost.</li>
 * </ul>
 * @param <I> The type of node ID
 * @param <N> The type of node objects
 */
public class Dijkstras<I, N extends GraphSearchNode<I>> extends GraphSearch<I, N> {

    public Dijkstras(Graph<I, N> graph) {
        super(graph);
    }

    /**
     * Find the highest cost route.
     * @param start The ID of the start node
     * @param end The ID of the end node
     * @return The maximum cost
     */
    // Implementation not stable at present
    public int findMaxCost(I start, I end) {
        //initPriorityQueue(true);
        //setIsDone(gsc -> gsc.getId().equals(end));
        
        //GraphSearchCache<I> result = solve(start);
        //Assert.that((result != null), "No result returned");
        
        //return result.getCostToHere();
        return 0;
    }

    /**
     * Find the lowest cost route.
     * @param start The ID of the start node
     * @param end The ID of the end node
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
