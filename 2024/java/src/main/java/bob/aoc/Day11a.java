package bob.aoc;

import bob.parser.LineListParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day11a extends BaseClass<List<List<Long>>> {

    public static void main(String[] args) {
        new Day11a().run(args, "");
    }

    public Day11a() {
        super(false);
        setParser(new LineListParser<>(" ", Long::valueOf));
    }

    @Override
    public void solve(List<List<Long>> data) {
        Assert.that((data.size() == 1), "More than one line read");
        List<Long> stones = data.get(0);
        LOG.info("Read {} stones", stones.size());
        
        LOG.debug("Stones: {}", stones);
        for (int i = 0; i < 25; i++) {
            List<Long> newStones = new ArrayList<>();
            
            for (Long s : stones) {
                String ss = s.toString();
                if (s == 0) {
                    newStones.add(1L);
                } else if ((ss.length() % 2) == 0) {
                    int half = ss.length() / 2;
                    newStones.add(Long.valueOf(ss.substring(0, half)));
                    newStones.add(Long.valueOf(ss.substring(half)));
                } else {
                    newStones.add(2024 * s);
                }
            }
            
            LOG.debug("Stones: {}", newStones);
            stones = newStones;
        }
        
        LOG.info("There are {} stones", stones.size());
    }
}
