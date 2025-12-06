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
        setParser(new ObjectParser<>(line -> new Day02Game(line, false)));
    }

    @Override
    public void solve(List<Day02Game> entries) {
        LOG.info("Read {} entries", entries.size());

        int score = 0;
        for (Day02Game e : entries) {
            score += e.me.getScore();
            if (e.me.getDefeats() == e.you) {
                score += 6;
            } else if (e.me == e.you) {
                score += 3;
            } else {
                score += 0;
            }
            LOG.debug("Game: me={}, you={}, score now {}", e.me, e.you, score);
        }

        LOG.info("Final score is {}", score);
    }
}
