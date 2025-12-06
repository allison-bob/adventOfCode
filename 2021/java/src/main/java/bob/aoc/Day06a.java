package bob.aoc;

import bob.parser.IntegerListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day06a extends BaseClass<List<Integer>> {

    public static void main(String[] args) {
        new Day06a().run(args, "");
    }

    public Day06a() {
        super(false);
        setParser(new IntegerListParser());
    }

    @Override
    public void solve(List<Integer> states) {
        LOG.info("read {} fish", states.size());
        
        Day06School school = new Day06School(states);
        
        for (int i = 0; i < 80; i++) {
            school.nextDay();
        }
        
        LOG.info("Answer is {}", school.size());
    }
}
