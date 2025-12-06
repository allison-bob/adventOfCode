package bob.aoc;

import bob.parser.LineObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Day09a extends BaseClass<List<List<Integer>>> {

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
        new Day09a().run(args, "");
    }

    public Day09a() {
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
        SortedMap<Integer, Segment> segments = new TreeMap<>();
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

        // Add an empty free space if necessary
        if (nextId == -1) {
            segments.put(pos, new Segment(0, -1));
        }
        LOG.debug(segments.toString());

        // Compact the disk
        int fe = findFirstEmpty(segments);
        while (fe < segments.lastKey()) {
            int endFree = segments.lastKey();
            int lastFull = segments.headMap(endFree).lastKey();
            LOG.debug("fe={}, endFree={}, lastFull={}", fe, endFree, lastFull);
            if (segments.get(lastFull).fileID == -1) {
                // Last full is actually free
                segments.get(lastFull).length += segments.get(endFree).length;
                segments.remove(endFree);
            } else if (segments.get(fe).length == segments.get(lastFull).length) {
                // First free space is same length as last file
                segments.get(fe).fileID = segments.get(lastFull).fileID;

                segments.get(lastFull).fileID = -1;
                segments.get(lastFull).length += segments.get(endFree).length;
                segments.remove(endFree);
            } else if (segments.get(fe).length > segments.get(lastFull).length) {
                // First free space is longer than last file
                int remain = segments.get(fe).length - segments.get(lastFull).length;
                segments.get(fe).fileID = segments.get(lastFull).fileID;
                segments.get(fe).length = segments.get(lastFull).length;
                segments.put((fe + segments.get(fe).length), new Segment(remain, -1));

                segments.get(lastFull).fileID = -1;
                segments.get(lastFull).length += segments.get(endFree).length;
                segments.remove(endFree);
            } else /* if (segments.get(fe).length < segments.get(lastFull).length) */ {
                // First free space is shorter than last file
                int remain = segments.get(lastFull).length - segments.get(fe).length;
                segments.get(fe).fileID = segments.get(lastFull).fileID;

                segments.get(lastFull).length -= segments.get(fe).length;
                segments.put((lastFull + remain), new Segment(segments.get(fe).length, -1));
            }
            LOG.debug(segments.toString());

            fe = findFirstEmpty(segments);
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

    private int findFirstEmpty(SortedMap<Integer, Segment> segments) {
        for (Integer i : segments.keySet()) {
            if (segments.get(i).fileID == -1) {
                return i;
            }
        }
        throw Assert.failed(null, "No free space on disk");
    }
}
