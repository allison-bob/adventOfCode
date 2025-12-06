package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day03a extends BaseClass<List<Day03Sack>> {

    public static void main(String[] args) {
        new Day03a().run(args, "");
    }

    public Day03a() {
        super(false);
        setParser(new ObjectParser<>(Day03Sack::new));
    }

    @Override
    public void solve(List<Day03Sack> sacks) {
        LOG.info("Read {} sacks", sacks.size());
        
        int sum = sacks.stream()
                .map(Day03Sack::findCommon)
                .mapToInt(Day03Sack::priority)
                .sum();
        
        LOG.info("Priority sum is {}", sum);
    }
}
