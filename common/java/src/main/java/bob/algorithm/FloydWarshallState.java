package bob.algorithm;

/**
 * A minimal interface for the state objects for the Floyd–Warshall algorithm.
 *
 * @param <I> The type of node ID
 */
public interface FloydWarshallState<I> {

    I getCurrentNode();
}
