package bob.aoc;

import bob.data.coordinate.Coord2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day24Floor {
    
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private enum Direction {
        NE(1, 1), NW(-1, 1), SE(1, -1), SW(-1, -1), E(2, 0), W(-2, 0);

        public final int dx;
        public final int dy;

        private Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private List<Coord2D> blackTiles = new ArrayList<>();

    public int countBlack() {
        return blackTiles.size();
    }

    public void doInitial(List<Coord2D> toFlip) {
        for (Coord2D c : toFlip) {
            if (blackTiles.contains(c)) {
                blackTiles.remove(c);
            } else {
                blackTiles.add(c);
            }
        }
    }
    
    public void doDaily() {
        // If the floor is now all white, we're done
        if (blackTiles.isEmpty()) {
            return;
        }
        
        // Find the scan boundaries
        int minX = blackTiles.stream().mapToInt(p -> p.getX()).min().getAsInt() - 1;
        int minY = blackTiles.stream().mapToInt(p -> p.getY()).min().getAsInt() - 1;
        int maxX = blackTiles.stream().mapToInt(p -> p.getX()).max().getAsInt() + 1;
        int maxY = blackTiles.stream().mapToInt(p -> p.getY()).max().getAsInt() + 1;
        LOG.debug("X from {} to {}, Y from {} to {}", minX, maxX, minY, maxY);
        
        // Scan the floor
        List<Coord2D> out = new ArrayList<>();
        for (int y = minY; y <= maxY; y++) {
            int dx = (((minX + y) % 2) == 0) ? 0 : 1;
            for (int x = (minX - dx); x <= (maxX + dx); x += 2) {
                // Find the number of neighboring black tiles
                long nct = findNeighbors(blackTiles, x, y);
                
                // Should this tile be black?
                Coord2D p = new Coord2D(x, y);
                if (blackTiles.contains(p)) {
                    if ((nct == 1) || (nct == 2)) {
                        out.add(p);
                    }
                } else {
                    if (nct == 2) {
                        out.add(p);
                    }
                }
            }
        }
        
        blackTiles = out;
    }
    
    // Need this in a separate method so x and y are effectively final
    private long findNeighbors(List<Coord2D> in, int x, int y) {
        return Stream.of(Direction.values())
                .map(d -> new Coord2D(x + d.dx, y + d.dy))
                .filter(p -> in.contains(p))
                .count();
    }
}
