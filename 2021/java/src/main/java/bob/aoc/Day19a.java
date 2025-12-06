package bob.aoc;

import bob.data.coordinate.Coord3D;
import bob.parser.PartObjectMapParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.stream.Collectors;

public class Day19a extends BaseClass<Map<String, List<Coord3D>>> {

    public static void main(String[] args) {
        new Day19a().run(args, "");
    }

    public Day19a() {
        super(false);
        setParser(new PartObjectMapParser<>(this::extractPoint, this::extractID));
    }

    private String extractID(String line) {
        String[] bits = line.split(" ");
        Assert.that((bits.length == 4), "wrong number of words on ID line");
        return bits[2];
    }

    private Coord3D extractPoint(String line) {
        String[] bits = line.split(",");
        Assert.that((bits.length == 3), "wrong number of words on coordinate line");
        return new Coord3D(Integer.parseInt(bits[0]), Integer.parseInt(bits[1]), Integer.parseInt(bits[2]));
    }

    @Override
    public void solve(Map<String, List<Coord3D>> data) {
        LOG.info("Read {} scanners", data.size());
        List<Day19Scanner> toLocate = data.entrySet().stream()
                .map(e -> new Day19Scanner(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        // Initialize the global scanner from the first scanner
        List<Day19Scanner> located = new ArrayList<>();
        Day19Scanner s = toLocate.remove(0);
        Day19Scanner global = new Day19Scanner(s);
        located.add(s);

        // Position and orient the remaining scanners
        while (!toLocate.isEmpty()) {
            s = findMatch(global, toLocate);
            LOG.debug("matched scanner {}", s.getId());
            located.add(s);
            toLocate.remove(s);
            s.getBeacons().forEach(global::addBeacon);
        }

        LOG.info("There are " + global.getDists().size() + " beacons");
    }

    private Day19Scanner findMatch(Day19Scanner global, List<Day19Scanner> toLocate) {
        for (Map.Entry<Coord3D, NavigableMap<Integer, Coord3D>> ge : global.getDists().entrySet()) {
            LOG.debug(">>> Looking for a match to global beacon {}", ge.getKey());
            NavigableMap<Integer, Coord3D> globalDists = ge.getValue();
            for (Day19Scanner toMatch : toLocate) {
                LOG.debug(">>>>>> Checking scanner {}", toMatch.getId());
                Day19Matcher matcher = new Day19Matcher(global, globalDists, toMatch);
                for (Map.Entry<Coord3D, NavigableMap<Integer, Coord3D>> me : toMatch.getDists().entrySet()) {
                    LOG.debug(">>>>>>>>> Checking local beacon {}", me.getKey());
                    if (matcher.matchOffsets(me.getValue())) {
                        if (matcher.findOrientation()) {
                            if (matcher.checkMatch()) {
                                return toMatch;
                            }
                        }
                    }
                }
            }
        }

        throw Assert.failed(null, "Unable to match any of the " + toLocate.size() + " remaining scanners");
    }
}
