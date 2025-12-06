package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day07a extends BaseClass<List<Day07Hand>> {

    public static void main(String[] args) {
        new Day07a().run(args, "");
    }

    public Day07a() {
        super(false);
        setParser(new ObjectParser<>(line -> new Day07Hand(line, false)));
    }

    @Override
    public void solve(List<Day07Hand> entries) {
        LOG.info("Read {} entries", entries.size());
        
        List<Day07Hand> sorted = new ArrayList<>(entries);
        sorted.sort(null);
        LOG.debug("hands are {}", sorted);

        long answer = 0;
        for (int i = 0; i < sorted.size(); i++) {
            answer += (i + 1) * sorted.get(i).bid;
        }
        
        LOG.info("answer is {}", answer);
    }
}
