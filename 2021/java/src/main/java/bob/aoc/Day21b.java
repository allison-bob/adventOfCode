package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

public class Day21b extends BaseClass<Map<String, Day21Player>> {

    @Getter
    private static class Possibility {

        private final int p1position;
        private final int p2position;
        private final int p1score;
        private final int p2score;

        public Possibility(int p1position, int p2position, int p1score, int p2score) {
            this.p1position = p1position;
            this.p2position = p2position;
            this.p1score = p1score;
            this.p2score = p2score;
        }

        @Override
        public String toString() {
            return "(" + p1position + "," + p2position + "," + p1score + "," + p2score + ")";
        }
    }

    @Getter
    @Setter
    private static class GameStates {

        private final Map<Possibility, Long> pending = new HashMap<>();
        private long p1wins;
        private long p2wins;

        public GameStates(int p1pos, int p2pos) {
            pending.put(new Possibility(p1pos, p2pos, 0, 0), 1L);
        }

        public GameStates(GameStates that) {
            this.p1wins = that.p1wins;
            this.p2wins = that.p2wins;
        }

        public void update(Possibility poss, long univct) {
            if (pending.containsKey(poss)) {
                pending.put(poss, pending.get(poss) + univct);
            } else {
                pending.put(poss, univct);
            }
        }

        @Override
        public String toString() {
            return p1wins + "," + p2wins + "," + pending;
        }
    }

    private static final Map<Integer, Integer> ROLL_OUTCOMES = new HashMap<>(7);

    static {
        // 1,1,1
        ROLL_OUTCOMES.put(3, 1);
        // 2,1,1   1,2,1   1,1,2
        ROLL_OUTCOMES.put(4, 3);
        // 3,1,1   1,3,1   1,1,3   2,2,1   2,1,2   1,2,2
        ROLL_OUTCOMES.put(5, 6);
        // 3,2,1   3,1,2   2,1,3   2,3,1   1,2,3   1,3,2   2,2,2
        ROLL_OUTCOMES.put(6, 7);
        // 3,3,1   3,1,3   1,3,3   3,2,2   2,3,2   2,2,3
        ROLL_OUTCOMES.put(7, 6);
        // 3,3,2   3,2,3   2,3,3
        ROLL_OUTCOMES.put(8, 3);
        // 3,3,3
        ROLL_OUTCOMES.put(9, 1);
    }

    public static void main(String[] args) {
        new Day21b().run(args, "");
    }

    public Day21b() {
        super(false);
        setParser(new LineObjectMapParser<>(Day21Player::new, Day21Player::getId));
    }

    @Override
    public void solve(Map<String, Day21Player> data) {
        LOG.info("Read {} players", data.size());
        Assert.that((data.size() == 2), "Wrong number of players");
        List<Day21Player> players = new ArrayList<>(data.values());

        // Initialize the possible game states
        GameStates currState = new GameStates(players.get(0).getPosition(), players.get(1).getPosition());
        LOG.debug("Start point: {}", currState);

        // Play the game in each universe
        boolean player1current = true;
        for (int i = 0; ((i < 20) && (!currState.getPending().isEmpty())); i++) {
            GameStates newState = new GameStates(currState);
            boolean p1 = player1current;

            currState.getPending().entrySet().forEach(e -> turn(newState, e.getKey(), e.getValue(), p1));

            currState = newState;
            player1current = !player1current;
        }

        // Find the answer
        LOG.info("Answer is {}", Math.max(currState.getP1wins(), currState.getP2wins()));
    }

    private void turn(GameStates newState, Possibility poss, long univCt, boolean player1current) {
        for (Map.Entry<Integer, Integer> r : ROLL_OUTCOMES.entrySet()) {
            if (player1current) {
                int newPos = ((poss.getP1position() + r.getKey() - 1) % 10) + 1;
                int newScore = newPos + poss.getP1score();
                if (newScore >= 21) {
                    // Player 1 won, add to the player's win count
                    newState.setP1wins(newState.getP1wins() + (univCt * r.getValue()));
                } else {
                    // No win yet, add to the pending game list
                    newState.update(
                            new Possibility(newPos, poss.getP2position(), newScore, poss.getP2score()),
                            (univCt * r.getValue())
                    );
                }
            } else {
                int newPos = ((poss.getP2position() + r.getKey() - 1) % 10) + 1;
                int newScore = newPos + poss.getP2score();
                if (newScore >= 21) {
                    // Player 2 won, add to the player's win count
                    newState.setP2wins(newState.getP2wins() + (univCt * r.getValue()));
                } else {
                    // No win yet, add to the pending game list
                    newState.update(
                            new Possibility(poss.getP1position(), newPos, poss.getP1score(), newScore),
                            (univCt * r.getValue())
                    );
                }
            }
        }
    }
}
