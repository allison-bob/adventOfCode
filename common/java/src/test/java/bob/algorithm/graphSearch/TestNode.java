package bob.algorithm.graphSearch;

import bob.data.coordinate.Coord2D;

/**
 * Node class for testing graph search algorithms.
 */
public class TestNode extends GraphSearchNode<Coord2D> {
    
    public TestNode(Coord2D id) {
        super(id);
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}
