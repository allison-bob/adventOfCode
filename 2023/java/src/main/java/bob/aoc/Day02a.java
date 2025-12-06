package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day02a extends BaseClass<List<Day02Game>> {

    public static void main(String[] args) {
        new Day02a().run(args, "");
    }

    public Day02a() {
        super(false);
        setParser(new ObjectParser<>(line -> new Day02Game(line)));
    }

    @Override
    public void solve(List<Day02Game> entries) {
        LOG.info("Read {} entries", entries.size());
        
        int idsum = 0;
        for (Day02Game game : entries) {
            boolean works = true;
            for (Day02Game.Draw draw : game.draws) {
                if ((draw.red > 12) || (draw.green > 13) || (draw.blue > 14)) {
                    works = false;
                }
            }
            if (works) {
                idsum += game.id;
            }
        }
        
        LOG.info("answer is {}", idsum);
    }
}
