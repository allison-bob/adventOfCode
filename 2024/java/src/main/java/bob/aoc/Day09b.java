package bob.aoc;

import bob.parser.LineObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Day09b extends BaseClass<List<List<Integer>>> {

    private class Segment {

        public int length;
        public int fileID;

        public Segment(int length, int fileID) {
            this.length = length;
            this.fileID = fileID;
        }

        @Override
        public String toString() {
            return "(" + length + "," + fileID + ")";
        }
    }

    public static void main(String[] args) {
        new Day09b().run(args, "");
    }

    public Day09b() {
        super(false);
        setParser(new LineObjectParser<>(c -> c - '0'));
    }

    @Override
    public void solve(List<List<Integer>> data) {
        Assert.that((data.size() == 1), "Read more than one line");
        List<Integer> entries = data.get(0);
        LOG.info("Read {} entries", entries.size());
        LOG.debug(entries.toString());

        // Build the list of segments
        TreeMap<Integer, Segment> segments = new TreeMap<>();
        int pos = 0;
        int id = 0;
        int nextId = 0;
        for (Integer e : entries) {
            segments.put(pos, new Segment(e, nextId));
            pos += e;
            if (nextId < 0) {
                nextId = id;
            } else {
                nextId = -1;
                id++;
            }
        }

        LOG.debug(segments.toString());

        // Compact the disk
        int toMove = segments.lastKey();
        while (toMove > 0) {
            if (segments.get(toMove).fileID != -1) {
                boolean looking = true;
                for (int f = 0; ((f < toMove) && looking); f = segments.higherKey(f)) {
                    if (segments.get(f).fileID == -1) {
                        LOG.debug("Checking {}:{} to {}:{}", toMove, segments.get(toMove), f, segments.get(f));
                        if (segments.get(f).length > segments.get(toMove).length) {
                            int remain = segments.get(f).length - segments.get(toMove).length;
                            segments.get(f).fileID = segments.get(toMove).fileID;
                            segments.get(f).length = segments.get(toMove).length;
                            segments.put((f + segments.get(f).length), new Segment(remain, -1));
                            segments.get(toMove).fileID = -1;
                            looking = false;
                        } else if (segments.get(toMove).length == segments.get(f).length) {
                            segments.get(f).fileID = segments.get(toMove).fileID;
                            segments.get(toMove).fileID = -1;
                            looking = false;
                        }
                    }
                }
            }
            toMove = segments.lowerKey(toMove);
            LOG.debug(segments.toString());
        }

        // Compute the checksum
        long sum = 0;
        for (Integer sumpos : segments.keySet()) {
            Segment s = segments.get(sumpos);
            if (s.fileID > -1) {
                for (int i = 0; i < s.length; i++) {
                    sum += (sumpos + i) * s.fileID;
                }
            }
        }

        LOG.info("Checksum is {}", sum);
    }
}
