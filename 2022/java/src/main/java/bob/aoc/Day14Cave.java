package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.util.Assert;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Day14Cave {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final Set<Coord2D> rock = new HashSet<>();
    private final Set<Coord2D> sand = new HashSet<>();
    private int maxY = -1;

    public void addData(String line) {
        String[] bits = line.split(" -> ");
        int x = -1;
        int y = -1;
        for (String b : bits) {
            String[] pieces = b.split(",");
            Assert.that((pieces.length == 2), "Wrong number of coordinate values");
            int dx = Integer.parseInt(pieces[0]);
            int dy = Integer.parseInt(pieces[1]);
            if (x == dx) {
                for (int i = Math.min(y, dy); i <= Math.max(y, dy); i++) {
                    rock.add(new Coord2D(x, i));
                }
            }
            if (y == dy) {
                for (int i = Math.min(x, dx); i <= Math.max(x, dx); i++) {
                    rock.add(new Coord2D(i, y));
                }
            }
            x = dx;
            y = dy;
            maxY = Math.max(y, maxY);
        }
    }

    public boolean addSand(Coord2D from) {
        Coord2D to = from;
        while (to.getY() < maxY) {
            Coord2D tst = findMove(to);
            if (tst == null) {
                sand.add(to);
                return true;
            }
            to = tst;
        }
        return false;
    }

    public boolean addSand2(Coord2D from) {
        if (sand.contains(from)) {
            return false;
        }

        Coord2D to = from;
        while (to.getY() < (maxY + 1)) {
            Coord2D tst = findMove(to);
            if (tst == null) {
                sand.add(to);
                return true;
            }
            to = tst;
        }
        
        rock.add(new Coord2D((to.getX() - 1), (maxY + 2)));
        rock.add(new Coord2D((to.getX() + 0), (maxY + 2)));
        rock.add(new Coord2D((to.getX() + 1), (maxY + 2)));
        Coord2D tst = findMove(to);
        Assert.that((tst == null), "Last move failed");
        sand.add(to);
        return true;
    }

    private Coord2D findMove(Coord2D from) {
        Coord2D tst = canMove(from, 0, 1);
        if (tst == null) {
            tst = canMove(from, -1, 1);
            if (tst == null) {
                tst = canMove(from, 1, 1);
            }
        }
        //LOG.debug("Move from {} is {}", from, tst);
        return tst;
    }

    private Coord2D canMove(Coord2D base, int dx, int dy) {
        Coord2D to = new Coord2D((base.getX() + dx), (base.getY() + dy));
        if (rock.contains(to)) {
            return null;
        }
        if (sand.contains(to)) {
            return null;
        }
        return to;
    }
}
