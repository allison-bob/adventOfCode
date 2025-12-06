package bob.data.graph;

import lombok.Getter;

/**
 * An edge between nodes in the graph.
 *
 * @param <I> The type of node IDs
 */
@Getter
public class Edge<I> {

    private final I from;
    private final I to;
    private final int cost;

    /**
     * Create an edge between the specified node IDs with cost of 0.
     *
     * @param from The ID of the node at the beginning of the edge
     * @param to The ID of the node at the end of the edge
     */
    public Edge(I from, I to) {
        this(from, to, 0);
    }

    /**
     * Create an edge between the specified node IDs.
     *
     * @param from The ID of the node at the beginning of the edge
     * @param to The ID of the node at the end of the edge
     * @param cost The cost (length, time, risk, ...) of the edge
     */
    public Edge(I from, I to, int cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "(" + from + "->" + to + "=" + cost + ")";
    }
}
