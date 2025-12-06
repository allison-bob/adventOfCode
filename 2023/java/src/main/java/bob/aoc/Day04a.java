package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day04a extends BaseClass<List<Day04Card>> {

    public static void main(String[] args) {
        new Day04a().run(args, "");
    }

    public Day04a() {
        super(false);
        setParser(new ObjectParser<>(line -> new Day04Card(line)));
    }

    @Override
    public void solve(List<Day04Card> entries) {
        LOG.info("Read {} entries", entries.size());
        
        long sum = 0;
        for (Day04Card c : entries) {
            int score = 0;
            for (Integer mynum : c.mynums) {
                if (c.winning.contains(mynum)) {
                    score = (score == 0) ? 1 : (score * 2);
                }
            }
            LOG.info("Card {} is worth {} points", c.id, score);
            sum += score;
        }
        
        LOG.info("answer is {}", sum);
    }
}
