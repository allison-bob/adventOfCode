package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day14a extends BaseClass<List<Day14Cave>> {

    public static void main(String[] args) {
        new Day14a().run(args, "");
    }

    public Day14a() {
        super(false);
        setParser(new ObjectListParser<>(Day14Cave::new, Day14Cave::addData));
    }

    @Override
    public void solve(List<Day14Cave> data) {
        Day14Cave cave = data.get(0);
        LOG.info("Read cave with {} rocks to depth {}", cave.getRock().size(), cave.getMaxY());
        
        while (cave.addSand(new Coord2D(500, 0)));
        
        LOG.info("Added {} sand", cave.getSand().size());
    }
}
