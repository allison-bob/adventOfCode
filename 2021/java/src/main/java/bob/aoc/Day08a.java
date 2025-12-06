package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.Set;

public class Day08a extends BaseClass<List<Day08Display>> {

    public static void main(String[] args) {
        new Day08a().run(args, "");
    }

    public Day08a() {
        super(false);
        setParser(new ObjectParser<>(Day08Display::new));
    }

    @Override
    public void solve(List<Day08Display> displays) {
        LOG.info("read {} displays", displays.size());
        
        long answer = displays.stream()
                .mapToLong(this::countUniques)
                .sum();

        LOG.info("Answer is {}", answer);
    }

    public long countUniques(Day08Display disp) {
        return disp.getOutputs().stream()
                .mapToInt(Set::size)
                .filter(i -> ((i == 2) || (i == 3) || (i == 4) || (i == 7)))
                .count();
    }
}
