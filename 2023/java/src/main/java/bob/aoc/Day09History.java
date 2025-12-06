package bob.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day09History {

    public List<List<Long>> history = new ArrayList<>();

    public Day09History(String line) {
        String[] bits = line.split(" ");
        history.add(new ArrayList<>(Stream.of(bits)
                .map(Long::valueOf)
                .toList()));
    }
    
    public boolean sequenceComplete() {
        List<Long> lasthist = history.get(history.size() - 1);
        return lasthist.stream()
                .allMatch(v -> v == 0);
    }
    
    public void findDiffs() {
        List<Long> lasthist = history.get(history.size() - 1);
        List<Long> diffs = new ArrayList<>();
        for (int i = 1; i < lasthist.size(); i++) {
            diffs.add(lasthist.get(i) - lasthist.get(i - 1));
        }
        history.add(diffs);
    }
    
    public long addNext() {
        long lastNew = 0;
        for (int i = history.size() - 1; i >= 0; i--) {
            List<Long> hist = history.get(i);
            lastNew += hist.get(hist.size() - 1);
            hist.add(lastNew);
        }
        return lastNew;
    }
    
    public long addFirst() {
        long lastNew = 0;
        for (int i = history.size() - 1; i >= 0; i--) {
            List<Long> hist = history.get(i);
            lastNew = hist.get(0) - lastNew;
            hist.add(0, lastNew);
        }
        return lastNew;
    }

    @Override
    public String toString() {
        return history.toString();
    }

    public String toStringLast() {
        return history.get(history.size() - 1).toString();
    }
}
