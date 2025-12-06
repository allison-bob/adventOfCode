package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.parser.LineObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.Map;

public class Day08a extends BaseClass<TwoFormatParser.Output<List<List<Day08Direction>>, Map<String, Day08Step>>> {

    public static void main(String[] args) {
        new Day08a().run(args, "a");
    }

    public Day08a() {
        super(false);
        setParser(new TwoFormatParser<>(
                new LineObjectParser<>(Day08Direction::byChar),
                new LineObjectMapParser<>(line -> new Day08Step(line), s -> s.id)
        ));
    }

    @Override
    public void solve(TwoFormatParser.Output<List<List<Day08Direction>>, Map<String, Day08Step>> data) {
        List<Day08Direction> directions = data.first.get(0);
        Map<String, Day08Step> steps = data.rest;
        LOG.info("Read {} directions, {} steps", directions.size(), steps.size());
        
        int count = 0;
        String currpos = "AAA";
        while (!currpos.equals("ZZZ")) {
            LOG.debug("step {}: at {}", count, steps.get(currpos));
            currpos = steps.get(currpos).next[directions.get(count % directions.size()).ordinal()];
            count++;
        }
        LOG.info("Trip took {} steps", count);
    }
}
