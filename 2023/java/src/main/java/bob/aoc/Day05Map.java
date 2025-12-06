package bob.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day05Map {
    
    private static class Entry {
        public long inLow;
        public long inHigh;
        public long outLow;

        public Entry(long inLow, long inHigh, long outLow) {
            this.inLow = inLow;
            this.inHigh = inHigh;
            this.outLow = outLow;
        }

        @Override
        public String toString() {
            return "in " + inLow + " to " + inHigh + " map starting at " + outLow;
        }
    }

    public String title;
    public List<Entry> entries = new ArrayList<>();
    
    public void addLine(String line) {
        if (line.endsWith("map:")) {
            title = line.substring(0, (line.length() - 5));
        } else {
            long[] bits = Stream.of(line.split(" "))
                    .mapToLong(Long::parseLong)
                    .toArray();
            entries.add(new Entry(bits[1], (bits[1] + bits[2] - 1), bits[0]));
        }
    }
    
    public long convert(long in) {
        long result = in;
        for (Entry e : entries) {
            if ((result >= e.inLow) && (result <= e.inHigh)) {
                return e.outLow + (result - e.inLow);
            }
        }
        return result;
    }
}
