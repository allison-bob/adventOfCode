package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day04b extends BaseClass<List<Day04Card>> {

    public static void main(String[] args) {
        new Day04b().run(args, "");
    }

    public Day04b() {
        super(false);
        setParser(new ObjectParser<>(line -> new Day04Card(line)));
    }

    @Override
    public void solve(List<Day04Card> entries) {
        LOG.info("Read {} entries", entries.size());
        
        for (int i = 0; i < entries.size(); i++) {
            Day04Card c = entries.get(i);
            
            int count = 0;
            for (Integer mynum : c.mynums) {
                if (c.winning.contains(mynum)) {
                    count++;
                }
            }
            LOG.info("Card {} makes {} copies of {} cards", c.id, count, c.count);
            
            for (int j = 0; j < count; j++) {
                entries.get(i + j + 1).count += c.count;
            }
        }
        
        int sum = entries.stream()
                .mapToInt(e -> e.count)
                .sum();
        LOG.info("answer is {}", sum);
    }
}
