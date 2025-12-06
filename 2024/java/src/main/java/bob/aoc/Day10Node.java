package bob.aoc;

import bob.algorithm.graphSearch.GraphSearchNode;
import bob.data.coordinate.Coord2D;

public class Day10Node extends GraphSearchNode<Coord2D> {
    
    public Day10Node(Coord2D id) {
        super(id);
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}
