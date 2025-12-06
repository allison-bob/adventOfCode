package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day23a extends BaseClass<List<Day23CupRing>> {

    public static void main(String[] args) {
        new Day23a().run(args, "");
    }

    public Day23a() {
        super(false);
        setParser(new ObjectListParser<>(() -> new Day23CupRing(9), Day23CupRing::add));
    }

    @Override
    public void solve(List<Day23CupRing> data) {
        Day23CupRing circle = data.get(0);
        LOG.info("Cup circle contains {} cups", (circle.getRing().length - 1));
        
        // Move the cups
        for (int i = 0; i < 100; i++) {
            circle.moveCups();
            LOG.debug("Move {}: {}", (i + 1), circle);
        }
        
        LOG.info("final: {}", circle);
    }
}
