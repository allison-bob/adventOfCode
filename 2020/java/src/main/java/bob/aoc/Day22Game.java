package bob.aoc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day22Game {

    private final boolean recursive;
    @Getter
    private final Map<Integer, Deque<Integer>> hands = new HashMap<>();
    private final Day22RoundCache cache = new Day22RoundCache();
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Constructor for initial game.
     * @param recursive {@code true} to play recursively
     * @param inHands The hands read from input data
     */
    public Day22Game(boolean recursive, Map<Integer, List<Integer>> inHands) {
        this.recursive = recursive;
        inHands.forEach((k, v) -> hands.put(k, new ArrayDeque<>(v)));
    }

    /**
     * Constructor for recursive game.
     * @param hands The current hands in the parent game
     * @param lengths The hand lengths to be used in this game
     */
    public Day22Game(Map<Integer, Deque<Integer>> hands, Map<Integer, Integer> lengths) {
        recursive = true;
        for (Integer p : hands.keySet()) {
            List<Integer> h = new ArrayList<>(hands.get(p));
            this.hands.put(p, new ArrayDeque<>(h.subList(0, lengths.get(p))));
        }
    }

    public int play() {
        LOG.debug("starting point:");
        hands.entrySet().forEach(e -> LOG.debug("{}: {}", e.getKey(), e.getValue()));

        // Play rounds until only one hand is empty
        while (hands.values().stream().allMatch(h -> !h.isEmpty())) {
            if (recursive) {
                // Stop infinite recursion
                if (cache.addRound(hands)) {
                    return 1;
                }
            }

            // Play the round
            round();
        }

        // Winner is the player with the most cards
        return hands.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
                .findFirst().get().getKey();
    }

    private void round() {
        // Draw cards
        Map<Integer, Integer> drawn = new HashMap<>();
        for (Integer h : hands.keySet()) {
            drawn.put(h, hands.get(h).removeFirst());
        }
        LOG.debug("   drawn: {}", drawn);

        // Find the winner
        Integer winner = findWinner(drawn);

        // Add the cards to the round's winner
        Deque<Integer> winningHand = hands.get(winner);
        winningHand.add(drawn.remove(winner));
        drawn.values().forEach(winningHand::add);

        LOG.debug("end of round:");
        hands.entrySet().forEach(e -> LOG.debug("{}: {}", e.getKey(), e.getValue()));
    }

    private Integer findWinner(Map<Integer, Integer> drawn) {
        if (recursive) {
            // Do we recurse?
            if (hands.entrySet().stream().allMatch(e -> e.getValue().size() >= drawn.get(e.getKey()))) {
                LOG.debug("********** New game begins **********");
                Day22Game g = new Day22Game(hands, drawn);
                int winner = g.play();
                LOG.debug("********** New game ends: {} **********", winner);
                return winner;
            }
        }
        
        // Sort the drawn cards
        List<Map.Entry<Integer, Integer>> sorted = drawn.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toList());
        LOG.debug("   sorted: {}", sorted);
        return sorted.get(0).getKey();
    }
}
