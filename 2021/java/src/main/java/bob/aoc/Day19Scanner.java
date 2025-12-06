package bob.aoc;

import bob.data.coordinate.Coord3D;
import bob.data.coordinate.Mapper3D;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Day19Scanner {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final String id;
    @Setter
    private Coord3D location;
    @Setter
    private Mapper3D orient = Mapper3D.ENU;
    // Map<[beacon1offset], Map<[beacon2dist], [beacon2offset]>>
    private final NavigableMap<Coord3D, NavigableMap<Integer, Coord3D>> dists = new TreeMap<>();

    public Day19Scanner(String id, List<Coord3D> beacons) {
        this.id = id;
        beacons.forEach(this::addBeacon);
    }

    public Day19Scanner(Day19Scanner src) {
        this.id = "global";
        this.location = new Coord3D(0, 0, 0);
        src.dists.keySet().forEach(this::addBeacon);
    }

    public void addBeacon(Coord3D offset) {
        if (!dists.containsKey(offset)) {
            // This is a new beacon, so work out the distances to all other beacons
            NavigableMap<Integer, Coord3D> d = new TreeMap<>();
            for (Coord3D dp : dists.keySet()) {
                int md = dp.manhattan(offset);
                d.put(md, dp);
                dists.get(dp).put(md, offset);
            }
            dists.put(offset, d);
        }
    }

    public Set<Coord3D> getBeacons() {
        LOG.debug("({}) Getting beacons, orient is {}, location is {}", id, orient.name(), location);
        Set<Coord3D> retval = new TreeSet<>();
        for (Coord3D b : dists.keySet()) {
            retval.add(orient.map(b).addOffset(location));
        }
        return retval;
    }

    @Override
    public String toString() {
        return "Scanner(" + id + "@" + location + "," + orient.name() + "," + dists + ")";
    }
}
