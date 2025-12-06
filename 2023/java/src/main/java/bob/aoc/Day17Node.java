package bob.aoc;

import bob.algorithm.AStarNode;
import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Day17Node implements AStarNode<Day17State> {

    public final Day17State state;
    public int costToHere = Integer.MAX_VALUE;
    public boolean visited;
    public final Coord2D target;
    private final Graph<Day17State, Day17Node> graph;
    private final Grid2D<Integer> map;

    //
    // Constructors
    //
    // Required for graph
    public Day17Node(Day17State state) {
        this.state = state;
        this.target = new Coord2D(0, 0);
        this.graph = null;
        this.map = null;
    }

    // Initial node
    public Day17Node(Coord2D target, Graph<Day17State, Day17Node> graph, Grid2D<Integer> map) {
        this.state = new Day17State(new Coord2D(0, 0), null, 0);
        this.target = target;
        this.graph = graph;
        this.map = map;
    }

    // Node moving from another node
    public Day17Node(Day17Node src, Day17Direction newdir) {
        this.state = new Day17State(src.state.location.addOffset(newdir.offset), newdir,
                ((src.state.direction == newdir) ? (src.state.linelen + 1) : 1));
        this.target = src.target;
        this.graph = src.graph;
        this.map = src.map;
    }
    
    public boolean isDone() {
        return state.location.equals(target);
    }

    @Override
    public Day17State getId() {
        return state;
    }

    @Override
    public String toString() {
        return state + "--" + costToHere;
    }

    @Override
    public int getEstCostToGoal() {
        return target.manhattan(state.location);
    }

//    @Override
//    public void setPathToHere(AStarNode<UUID> from) {
//        AStarNode.super.setPathToHere(from);
//    }

    @Override
    public void visit() {
        moveCheck(Day17Direction.N);
        moveCheck(Day17Direction.S);
        moveCheck(Day17Direction.E);
        moveCheck(Day17Direction.W);
    }

    private void moveCheck(Day17Direction desired) {
        if ((state.direction != null) && (desired == state.direction.reverse)) {
            return;
        }
        if ((desired == state.direction) && (state.linelen > 2)) {
            return;
        }
        if (map.get(state.location.addOffset(desired.offset)) == null) {
            return;
        }
        
        Coord2D newloc = state.location.addOffset(desired.offset);
        Day17Node newnode = new Day17Node(this, desired);
        
        graph.addNode(newnode);
        graph.addEdge(state, newnode.state, map.get(newnode.state.location));
    }
}
