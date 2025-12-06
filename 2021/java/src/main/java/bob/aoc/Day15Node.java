package bob.aoc;

import bob.algorithm.AStarNode;
import bob.data.coordinate.Coord2D;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Day15Node implements AStarNode<Coord2D> {

    private Coord2D id;
    private int input;
    private int estCostToGoal;
    
    @Setter
    private int costToHere = Integer.MAX_VALUE;
    
    @Setter
    private boolean visited;

    public Day15Node(char in) {
        this.input = in - '0';
    }

    public Day15Node(Coord2D id) {
        this.id = id;
    }
    
    public void setPosition(int x, int y, int maxX, int maxY) {
        id = new Coord2D(x, y);
        estCostToGoal = (maxX - x) + (maxY - y);
    }
    
    public Day15Node expandedNode(int expand) {
        Day15Node retval = new Day15Node('0');
        retval.input = this.input + expand;
        if (retval.input > 9) {
            retval.input -= 9;
        }
        return retval;
    }

    @Override
    public String toString() {
        return "" + input;
    }
}
