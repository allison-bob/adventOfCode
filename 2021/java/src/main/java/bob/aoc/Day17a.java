package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day17a extends BaseClass<List<Day17Target>> {

    public static void main(String[] args) {
        new Day17a().run(args, "");
    }

    public Day17a() {
        super(false);
        setParser(new ObjectParser<>(Day17Target::new));
    }

    @Override
    public void solve(List<Day17Target> targets) {
        Day17Target target = targets.get(0);
        LOG.info("Read target {}", target);
        
        // Since we are going for maximum height, assume x velocity will go to 0.
        // This places the range of initial x velocity to values where sum(1..n) is in target area
        int maxY = target.getYMin();
        int finalX = 0;
        for (int initX = 1; finalX < target.getXMax(); initX++) {
            finalX += initX;
            if ((finalX >= target.getXMin()) && (finalX <= target.getXMax())) {
                int y = testLaunch(target, initX);
                LOG.debug("Max height for x={} is {}", initX, y);
                maxY = Math.max(maxY, y);
            }
        }
        
        LOG.info("Maximum height is {}", maxY);
    }
    
    public int testLaunch(Day17Target target, int initX) {
        int maxY = target.getYMin();
        int maxInitY = 7 * (target.getYMax() - target.getYMin());
        for (int initY = 0; initY < maxInitY; initY++) {
                int y = target.doLaunch(initX, initY);
                maxY = Math.max(maxY, y);
        }
        return maxY;
    }
}
