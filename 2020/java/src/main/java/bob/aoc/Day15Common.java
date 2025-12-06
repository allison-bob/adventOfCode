package bob.aoc;

import bob.parser.IntegerListParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15Common extends BaseClass<List<Integer>> {

    private final int roundCt;
    private final Map<Integer, Day15Spoken> spoken = new HashMap<>();

    public Day15Common(int roundCt, boolean debug) {
        super(debug);
        this.roundCt = roundCt;
        setParser(new IntegerListParser());
    }

    @Override
    public void solve(List<Integer> starters) {
        LOG.info("read {} starter numbers", starters.size());
        Assert.that((starters.size() > 1), "Not enough starters");
        int lastSaid = 0;

        // Speak the starting numbers
        for (int i = 0; i < starters.size(); i++) {
            lastSaid = starters.get(i);
            speak(i, lastSaid);
        }

        // Speak the remaining numbers
        for (int turn = starters.size(); turn < roundCt; turn++) {
            Day15Spoken last = spoken.get(lastSaid);
            if (last.notSaidBefore()) {
                lastSaid = 0;
            } else {
                lastSaid = last.lastSaidAge();
            }
            speak(turn, lastSaid);
        }

        LOG.info("last said is {}", lastSaid);
    }

    private void speak(int turn, int value) {
        if (spoken.containsKey(value)) {
            spoken.get(value).wasSaid(turn);
        } else {
            spoken.put(value, new Day15Spoken(turn));
        }
        LOG.debug("turn={}, spoken={}", turn, value);
    }
}
