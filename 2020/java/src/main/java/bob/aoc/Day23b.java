package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day23b extends BaseClass<List<Day23CupRing>> {

    public static void main(String[] args) {
        new Day23b().run(args, "");
    }

    public Day23b() {
        super(false);
        setParser(new ObjectListParser<>(() -> new Day23CupRing(1000000), Day23CupRing::add));
    }

    @Override
    public void solve(List<Day23CupRing> data) {
        Day23CupRing circle = data.get(0);
        LOG.info("Cup circle contains {} cups", (circle.getRing().length - 1));
        
        // Move the cups
        for (int i = 0; i < 10000000; i++) {
            circle.moveCups();
            LOG.debug("Move {}:", (i + 1));
        }
        
        int a = circle.getRing()[1];
        int b = circle.getRing()[a];
        LOG.info("final: {} * {} = {}", a, b, ((long) a * b));
    }
}
