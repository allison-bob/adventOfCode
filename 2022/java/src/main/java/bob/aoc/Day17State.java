package bob.aoc;

import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Day17State {

    private final int[] columnDepth = new int[] {-1,-1,-1,-1,-1,-1,-1};
    private final Day17Rock currRock;
    private final int currDir;

    public Day17State(Day17Rock currRock, int currDir) {
        this.currRock = currRock;
        this.currDir = currDir;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Arrays.hashCode(this.columnDepth);
        hash = 89 * hash + Objects.hashCode(this.currRock);
        hash = 89 * hash + this.currDir;
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
        final Day17State other = (Day17State) obj;
        if (this.currDir != other.currDir) {
            return false;
        }
        if (!Arrays.equals(this.columnDepth, other.columnDepth)) {
            return false;
        }
        return this.currRock == other.currRock;
    }

    @Override
    public String toString() {
        return "{dir=" + currDir + ",rock=" + currRock.name() + "," + Arrays.toString(columnDepth) + "}";
    }
}
