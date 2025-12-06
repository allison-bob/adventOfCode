package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day05a extends BaseClass<List<Day05Line>> {

    public static void main(String[] args) {
        new Day05a().run(args, "");
    }

    public Day05a() {
        super(false);
        setParser(new ObjectParser<>(Day05Line::new));
    }

    @Override
    public void solve(List<Day05Line> lines) {
        int maxval = lines.stream()
                .mapToInt(Day05Line::maxCoord)
                .reduce(0, Math::max);
        LOG.info("read {} lines, map size is {}", lines.size(), maxval);
        
        Day05Map map = new Day05Map(maxval, LOG);
        lines.forEach(l -> map.plot(l, false));

        int count = map.countAbove(1);
        LOG.info("Answer is {}", count);
    }
}
