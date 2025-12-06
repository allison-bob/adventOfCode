package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day15a extends BaseClass<List<Day15Sensor>> {

    public static void main(String[] args) {
        new Day15a().run(args, "");
    }

    public Day15a() {
        super(false);
        setParser(new ObjectParser<>(Day15Sensor::new));
    }

    @Override
    public void solve(List<Day15Sensor> sensors) {
        LOG.info("read {} sensors", sensors.size());

        int answer = countSpots(sensors, (isRealData() ? 2000000 : 10));

        LOG.info("Answer is {}", answer);
    }

    private int countSpots(List<Day15Sensor> sensors, int y) {
        List<int[]> ranges = sensors.stream()
                .filter(s -> s.findCoverage(y))
                .peek(s -> LOG.debug("Sensor at {} covers from {} to {}", s.getPos(), s.getCoverLo(), s.getCoverHi()))
                .map(s -> new int[]{s.getCoverLo(), s.getCoverHi()})
                .collect(Collectors.toList());
        int rsize = ranges.size() + 1;
        while (rsize > ranges.size()) {
            LOG.debug("Ranges are {}", dump(ranges));
            rsize = ranges.size();
            ranges = coalesce(ranges);
        }
        LOG.debug("Ranges are {}", dump(ranges));
        
        Set<Coord2D> beacons = sensors.stream()
                .map(Day15Sensor::getBeacon)
                .filter(b -> b.getY() == y)
                .collect(Collectors.toSet());
        LOG.debug("Beacons on target y: {}", beacons);

        int ct = 0;
        for (int[] r : ranges) {
            ct += r[1] - r[0] + 1;
        }
        return ct - beacons.size();
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
