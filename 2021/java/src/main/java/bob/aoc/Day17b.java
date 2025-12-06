package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day17b extends BaseClass<List<Day17Target>> {

    public static void main(String[] args) {
        new Day17b().run(args, "");
    }

    public Day17b() {
        super(false);
        setParser(new ObjectParser<>(Day17Target::new));
    }

    @Override
    public void solve(List<Day17Target> targets) {
        Day17Target target = targets.get(0);
        LOG.info("Read target {}", target);

        // Find all possible trajectories
        int maxInitY = 7 * (target.getYMax() - target.getYMin());
        Set<Coord2D> hits = new HashSet<>();
        for (int initX = 0; initX <= target.getXMax(); initX++) {
            for (int initY = target.getYMin(); initY <= maxInitY; initY++) {
                if (target.doLaunch(initX, initY) >= target.getYMin()) {
                    LOG.debug("Hit at initial velocity {},{}", initX, initY);
                    hits.add(new Coord2D(initX, initY));
                }
            }
        }
        
        LOG.info("Number of hits is {}", hits.size());
    }
}
