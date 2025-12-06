package bob.aoc;

import bob.data.coordinate.Coord2D;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Day23Amphipod {

    private final Coord2D location;
    private final Day23AmphipodType type;
    @Setter
    private boolean inFinalSpot;

    public Day23Amphipod(int x, int y, char type) {
        this.location = new Coord2D(x, y);
        this.type = Day23AmphipodType.byChar(type);
    }
    
    public String getId() {
        return "" + type.getSymbol() + location;
    }

    @Override
    public String toString() {
        return type.getSymbol() + "@" + location + ":" + isInFinalSpot();
    }

    /**
     * @return the inFinalSpot
     */
    public boolean isInFinalSpot() {
        return inFinalSpot;
    }
}
