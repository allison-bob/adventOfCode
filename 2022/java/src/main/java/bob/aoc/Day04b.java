package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day04b extends BaseClass<List<Day04Assignment>> {

    public static void main(String[] args) {
        new Day04b().run(args, "");
    }

    public Day04b() {
        super(false);
        setParser(new ObjectParser<>(Day04Assignment::new));
    }

    @Override
    public void solve(List<Day04Assignment> assigns) {
        LOG.info("Read {} assignments", assigns.size());
        
        long count = assigns.stream()
                .filter(Day04Assignment::partialOverlap)
                .count();
        
        LOG.info("There are {} partial overlaps", count);
    }
}
