package bob.aoc;

import bob.algorithm.DepthFirstSearchNode;
import bob.data.coordinate.Coord2D;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Day24Node implements DepthFirstSearchNode<UUID> {

    private final UUID id;
    private final Coord2D expedLoc;
    private final int depth;
    private final int estCostToGoal;

    public Day24Node(UUID id) {
        this.id = id;
        this.expedLoc = null;
        this.depth = 0;
        this.estCostToGoal = 0;
    }

    public Day24Node(Coord2D expedLoc, int depth, int estCostToGoal) {
        this.id = UUID.randomUUID();
        this.expedLoc = expedLoc;
        this.depth = depth;
        this.estCostToGoal = estCostToGoal;
    }

    @Override
    public int getCostToHere() {
        return depth;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.expedLoc);
        hash = 59 * hash + this.depth;
        hash = 59 * hash + this.estCostToGoal;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Day24Node other = (Day24Node) obj;
        if (this.depth != other.depth) {
            return false;
        }
        if (this.estCostToGoal != other.estCostToGoal) {
            return false;
        }
        return Objects.equals(this.expedLoc, other.expedLoc);
    }

    @Override
    public String toString() {
        return "{exp@" + expedLoc + ",depth=" + depth + ",costToGoal=" + estCostToGoal + "}";
    }
}
