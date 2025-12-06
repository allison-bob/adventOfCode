package bob.aoc;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day22RoundCache {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    // ArrayDeque.equals does not test content, causing the repeat check to fail
    private final List<Map<Integer, List<Integer>>> rounds = new ArrayList<>();

    public boolean addRound(Map<Integer, Deque<Integer>> round) {
        // Make a copy of the current round
        Map<Integer, List<Integer>> copy = new HashMap<>();
        for (Integer p : round.keySet()) {
            copy.put(p, new ArrayList<>(round.get(p)));
        }

        // Check the current round against the cache
        for (Map<Integer, List<Integer>> r : rounds) {
            if (r.equals(copy)) {
                LOG.debug("This round is a repeat");
                LOG.debug("Cached round: {}", r);
                LOG.debug("The new round: {}", round);
                return true;
            }
        }

        rounds.add(copy);
        return false;
    }
}
