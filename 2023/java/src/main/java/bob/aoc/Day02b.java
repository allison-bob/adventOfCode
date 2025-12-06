package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day02b extends BaseClass<List<Day02Game>> {

    public static void main(String[] args) {
        new Day02b().run(args, "");
    }

    public Day02b() {
        super(false);
        setParser(new ObjectParser<>(line -> new Day02Game(line)));
    }

    @Override
    public void solve(List<Day02Game> entries) {
        LOG.info("Read {} entries", entries.size());

        long powersum = 0;
        for (Day02Game game : entries) {
            long minred = 0;
            long mingreen = 0;
            long minblue = 0;
            for (Day02Game.Draw draw : game.draws) {
                minred = Math.max(minred, draw.red);
                mingreen = Math.max(mingreen, draw.green);
                minblue = Math.max(minblue, draw.blue);
            }
            powersum += (minred * mingreen * minblue);
        }

        LOG.info("answer is {}", powersum);
    }
}
