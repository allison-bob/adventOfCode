package bob.data.graph;

import bob.util.ObjectBuilder;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A directed Graph database, consisting of nodes and edges to connect the nodes.
 *
 * @param <I> The type of node ID
 * @param <N> The type of node objects
 */
public class Graph<I, N extends Node<I>> {

    // Constructors for objects
    private final Constructor<N> nodeConstruct;

    private final Map<I, N> nodes = new HashMap<>();
    private final List<Edge<I>> edges = new ArrayList<>();

    public Graph(Class<I> idClass, Class<N> nodeClass) {
        this.nodeConstruct = ObjectBuilder.getConstructor(nodeClass, idClass);
    }

    /**
     * Add an edge to the graph.
     *
     * @param from The ID of the node at the beginning of the edge
     * @param to The ID of the node at the end of the edge
     * @param cost The cost (length, time, risk, ...) of the edge
     */
    public void addEdge(I from, I to, int cost) {
        edges.add(new Edge<>(from, to, cost));
    }

    /**
     * Build edges from the array of string node IDs at cost 0.
     *
     * @param bidirect {@code true} if edges should be created in both directions
     * @param ids The array of node IDs to create edges between
     */
    public void addEdges(boolean bidirect, I[] ids) {
        // Create all required nodes
        for (I id : ids) {
            if (!nodes.containsKey(id)) {
                nodes.put(id, ObjectBuilder.getInstance(nodeConstruct, id));
            }
        }

        // Create the identified edges
        for (int i = 1; i < ids.length; i++) {
            edges.add(new Edge<>(ids[i - 1], ids[i]));
            if (bidirect) {
                edges.add(new Edge<>(ids[i], ids[i - 1]));
            }
        }
    }

    /**
     * Add a node to the graph. Note that this method will replace any existing node with the same ID.
     *
     * @param node The node to add
     */
    public void addNode(N node) {
        nodes.put(node.getId(), node);
    }

    /**
     * Retrieve the node with the specified ID.
     *
     * @param id The ID to retrieve
     * @return The node object or {@code null} if the ID does not exist
     */
    public N get(I id) {
        return nodes.get(id);
    }

    /**
     * Retrieve the number of edges in the graph.
     *
     * @return The edge count
     */
    public int getEdgeCt() {
        return edges.size();
    }

    /**
     * Retrieve the list of nodes that are directly connected to the specified node.
     *
     * @param id The ID of interest
     * @return The list of neighboring nodes
     */
    public List<N> getNeighbors(I id) {
        List<N> retval = new ArrayList<>();

        for (Edge<I> e : edges) {
            if (e.getFrom().equals(id)) {
                retval.add(nodes.get(e.getTo()));
            }
        }

        return retval;
    }

    /**
     * Retrieve the nodes that are directly connected to the specified node and the associated cost.
     *
     * @param id The ID of interest
     * @return The map of neighboring nodes to cost
     */
    public Map<N, Integer> getNeighborMap(I id) {
        Map<N, Integer> retval = new HashMap<>();

        for (Edge<I> e : edges) {
            if (e.getFrom().equals(id)) {
                retval.put(nodes.get(e.getTo()), e.getCost());
            }
        }

        return retval;
    }

    /**
     * Retrieve the number of nodes in the graph.
     *
     * @return The node count
     */
    public int getNodeCt() {
        return nodes.size();
    }

    /**
     * Create a stream of the nodes in the graph.
     *
     * @return The node stream
     */
    public Stream<N> nodeStream() {
        return nodes.values().stream();
    }

    @Override
    public String toString() {
        return "Graph:\n   Nodes:" + nodes.values() + "\n   Edges:" + edges;
    }
}
