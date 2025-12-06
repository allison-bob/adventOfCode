package bob.aoc;

import bob.algorithm.AStarNode;
import bob.data.coordinate.Coord2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Day12Spot implements AStarNode<Coord2D> {

    private Coord2D id;
    private char input;
    private int estCostToGoal;
    private int costToHere = Integer.MAX_VALUE;
    private boolean visited;

    public Day12Spot(char in) {
        this.input = in;
    }

    public Day12Spot(Coord2D id) {
        this.id = id;
    }

    public String forDump() {
        return String.valueOf(input);
    }

    @Override
    public String toString() {
        return "" + id + ":" + input;
    }
}
