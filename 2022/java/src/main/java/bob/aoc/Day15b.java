package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day15b extends BaseClass<List<Day15Sensor>> {

    public static void main(String[] args) {
        new Day15b().run(args, "");
    }

    public Day15b() {
        super(false);
        setParser(new ObjectParser<>(Day15Sensor::new));
    }

    @Override
    public void solve(List<Day15Sensor> sensors) {
        LOG.info("read {} sensors", sensors.size());

        for (int y = 0; y < (isRealData() ? 4000000 : 20); y++) {
            List<int[]> ranges = countSpots(sensors, y);
            if (ranges.size() > 1) {
                LOG.debug("At y={}, ranges are {}", y, dump(ranges));
                int[] r0 = ranges.get(0);
                int[] r1 = ranges.get(1);
                int x = 0;
                if ((r1[0] - r0[1]) == 2) {
                    x = r0[1] + 1;
                }
                if ((r0[0] - r1[1]) == 2) {
                    x = r1[1] + 1;
                }
                LOG.info("Beacon at {},{}, answer is {}", x, y, ((x * 4000000L) + y));
            }
        }

//        LOG.info("Answer is {}", answer);
    }

    private List<int[]> countSpots(List<Day15Sensor> sensors, int y) {
        List<int[]> ranges = sensors.stream()
                .filter(s -> s.findCoverage(y))
                //.peek(s -> LOG.debug("Sensor at {} covers from {} to {}", s.getPos(), s.getCoverLo(), s.getCoverHi()))
                .map(s -> new int[]{s.getCoverLo(), s.getCoverHi()})
                .collect(Collectors.toList());
        int rsize = ranges.size() + 1;
        while (rsize > ranges.size()) {
            //LOG.debug("Ranges are {}", dump(ranges));
            rsize = ranges.size();
            ranges = coalesce(ranges);
        }
        return ranges;
    }

    private List<int[]> coalesce(List<int[]> in) {
        List<int[]> out = new ArrayList<>();
        for (int[] i : in) {
            boolean found = false;
            for (int[] o : out) {
                if ((i[0] >= o[0]) && (i[0] <= o[1])) {
                    // Input low is within this output, push high out
                    o[1] = Math.max(o[1], i[1]);
                    found = true;
                }
                if ((i[1] >= o[0]) && (i[1] <= o[1])) {
                    // Input high is within this output, push low out
                    o[0] = Math.min(o[0], i[0]);
                    found = true;
                }
                if (i[1] == (o[0] - 1)) {
                    // Input high is next to this output, push high out
                    o[1] = Math.max(o[1], i[1]);
                    found = true;
                }
                if ((o[0] >= i[0]) && (o[0] <= i[1])) {
                    // Output low is within this input, push high out
                    o[1] = Math.max(o[1], i[1]);
                    found = true;
                }
                if ((o[1] >= i[0]) && (o[1] <= i[1])) {
                    // Output high is within this input, push low out
                    o[0] = Math.min(o[0], i[0]);
                    found = true;
                }
                if (o[1] == (i[0] - 1)) {
                    // Output high is next to this input, push high out
                    o[1] = Math.max(o[1], i[1]);
                    found = true;
                }
            }
            if (!found) {
                out.add(i);
            }
        }
        return out;
    }

    private String dump(List<int[]> in) {
        StringBuilder sb = new StringBuilder("(");
        for (int[] i : in) {
            if (sb.length() > 2) {
                sb.append(",");
            }
            sb.append(Arrays.toString(i));
        }
        return sb.append(")").toString();
    }
}
