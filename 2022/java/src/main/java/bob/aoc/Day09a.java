package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day09a extends BaseClass<List<Day09Move>> {

    public static void main(String[] args) {
        new Day09a().run(args, "");
    }

    public Day09a() {
        super(false);
        setParser(new ObjectParser<>(Day09Move::new));
    }

    @Override
    public void solve(List<Day09Move> moves) {
        LOG.info("read {} moves", moves.size());
        
        Set<Coord2D> visited = new HashSet<>();
        Coord2D headPos = new Coord2D(0, 0);
        Coord2D tailPos = new Coord2D(0, 0);
        visited.add(tailPos);
        
        for (Day09Move move : moves) {
            LOG.debug("Processing {}", move);
            for (int i = 0; i < move.getLength(); i++) {
                headPos = moveHead(headPos, move.getDir());
                tailPos = moveTail(tailPos, headPos);
                LOG.debug("Head now {}, tail now {}", headPos, tailPos);
                visited.add(tailPos);
            }
        }

        LOG.info("The tail visited {} points", visited.size());
    }
    
    private Coord2D moveHead(Coord2D curr, Day09Move.Direction dir) {
        return new Coord2D((curr.getX() + dir.getDx()), (curr.getY() + dir.getDy()));
    }
    
    private Coord2D moveTail(Coord2D curr, Coord2D head) {
        if ((Math.abs(curr.getX() - head.getX()) < 2) && (Math.abs(curr.getY() - head.getY()) < 2)) {
            // Close enough, no move needed
            return curr;
        }
        
        if (curr.getX() == head.getX()) {
            // Head is two steps above or below
            int dy = (head.getY() - curr.getY()) / 2;
            return new Coord2D(curr.getX(), (curr.getY() + dy));
        }
        
        if (curr.getY() == head.getY()) {
            // Head is two steps left or right
            int dx = (head.getX() - curr.getX()) / 2;
            return new Coord2D((curr.getX() + dx), curr.getY());
        }
        
        // Move diagonally
        int dx = (head.getX() - curr.getX()) / 2;
        dx = (dx == 0) ? (head.getX() - curr.getX()) : dx;
        int dy = (head.getY() - curr.getY()) / 2;
        dy = (dy == 0) ? (head.getY() - curr.getY()) : dy;
        return new Coord2D((curr.getX() + dx), (curr.getY() + dy));
    }
}
