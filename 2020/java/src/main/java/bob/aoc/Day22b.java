package bob.aoc;

import bob.parser.PartObjectMapParser;
import bob.util.BaseClass;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22b extends BaseClass<Map<Integer, List<Integer>>> {

    public static void main(String[] args) {
        new Day22b().run(args, "");
    }

    public Day22b() {
        super(false);
        setParser(new PartObjectMapParser<>(Integer::valueOf, this::parseID));
    }

    private Integer parseID(String line) {
        int pos = line.indexOf(":");
        return Integer.valueOf(line.substring(7, pos));
    }

    @Override
    public void solve(Map<Integer, List<Integer>> hands) {
        LOG.info("Read {} hands", hands.size());
        Day22Game game = new Day22Game(true, hands);

        // Play the game
        int winner = game.play();

        // Calculate scores
        Map<Integer, Long> scores = new HashMap<>();
        for (Map.Entry<Integer, Deque<Integer>> e : game.getHands().entrySet()) {
            long score = 0;
            Deque<Integer> hand = e.getValue();
            int len = hand.size();
            for (int i = 1; i <= len; i++) {
                score += (i * hand.removeLast());
            }
            scores.put(e.getKey(), score);
        }
        LOG.info("scores: {}", scores);
    }
}
