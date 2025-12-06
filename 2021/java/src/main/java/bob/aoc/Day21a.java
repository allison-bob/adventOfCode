package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day21a extends BaseClass<Map<String, Day21Player>> {
    
    private static class Die {

        private int lastval;
        private int rollct;

        public int getRollct() {
            return rollct;
        }

        public int roll() {
            rollct++;
            lastval++;
            if (lastval > 100) {
                lastval -= 100;
            }
            return lastval;
        }
    }

    public static void main(String[] args) {
        new Day21a().run(args, "");
    }

    public Day21a() {
        super(false);
        setParser(new LineObjectMapParser<>(Day21Player::new, Day21Player::getId));
    }

    @Override
    public void solve(Map<String, Day21Player> data) {
        LOG.info("Read {} players", data.size());
        Assert.that((data.size() == 2), "Wrong number of players");
        List<Day21Player> players = new ArrayList<>(data.values());
        Die die = new Die();

        // Play the game
        playGame(players, die);
        
        // Find the answer
        Day21Player loser = (players.get(0).getScore() < 1000) ? players.get(0) : players.get(1);
        long answer = loser.getScore() * die.getRollct();

        LOG.info("Player {} lost with score {}", loser.getId(), loser.getScore());
        LOG.info("Die was rolled {} times, yielding an answer of {}", die.getRollct(), answer);
    }

    private void playGame(List<Day21Player> players, Die die) {
        for (int i = 0; i < 1000; i++) {
            for (Day21Player p : players) {
                if (p.move(die.roll() + die.roll() + die.roll(), 1000)) {
                    return;
                }
            }
        }
    }
}
