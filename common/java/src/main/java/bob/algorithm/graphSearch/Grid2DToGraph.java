package bob.algorithm.graphSearch;

import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.graph.Node;
import bob.data.grid.Grid2D;
import bob.util.ObjectBuilder;
import java.lang.reflect.Constructor;
import java.util.function.BiFunction;

/**
 * A utility class to build a graph from the 2D grid provided as puzzle input.
 */
public class Grid2DToGraph {

    public static <T, N extends Node<Coord2D>> Graph<Coord2D, N> convert(
            Class<N> nodeClass,
            Grid2D<T> input,
            BiFunction<T, T, Integer> edgeCost
    ) {
        Graph<Coord2D, N> output = new Graph<>(Coord2D.class, nodeClass);
        Constructor<N> nodeConstruct = ObjectBuilder.getConstructor(nodeClass, Coord2D.class);
        
        for (int y = 0; y < input.getSize().getY(); y++) {
            for (int x = 0; x < input.getSize().getX(); x++) {
                output.addNode(ObjectBuilder.getInstance(nodeConstruct, new Coord2D(x, y)));
                if (x > 0) {
                    makeEdge(output, input, (x-1), y, x, y, edgeCost);
                    makeEdge(output, input, x, y, (x-1), y, edgeCost);
                }
                if (y > 0) {
                    makeEdge(output, input, x, (y-1), x, y, edgeCost);
                    makeEdge(output, input, x, y, x, (y-1), edgeCost);
                }
            }
        }
        
        return output;
    }

    private static <T, N extends Node<Coord2D>> void makeEdge(Graph<Coord2D, N> output, Grid2D<T> input,
                                     int x1, int y1, int x2, int y2, BiFunction<T, T, Integer> edgeCost) {
        int cost = edgeCost.apply(input.get(x1, y1), input.get(x2, y2));
        if (cost >= 0) {
            output.addEdge(new Coord2D(x1, y1), new Coord2D(x2, y2), cost);
        }
    }
}
