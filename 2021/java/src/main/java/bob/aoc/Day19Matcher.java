package bob.aoc;

import bob.data.coordinate.Coord3D;
import bob.data.coordinate.Mapper3D;
import java.util.Arrays;
import java.util.NavigableMap;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day19Matcher {

    private static final int REQUIRED_MATCHES = 8;
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    // Information about the global space
    private final Day19Scanner global;
    private final NavigableMap<Integer, Coord3D> globalDists;
    private final Coord3D[] globalPoints = new Coord3D[REQUIRED_MATCHES];

    // Information on the scanner we are trying to match
    private final Day19Scanner toMatch;
    private final Coord3D[] matchPoints = new Coord3D[REQUIRED_MATCHES];
    private final Coord3D[] mapMatchPoints = new Coord3D[REQUIRED_MATCHES];
    private Mapper3D matchMapper;

    public Day19Matcher(Day19Scanner global, NavigableMap<Integer, Coord3D> globalDists, Day19Scanner toMatch) {
        this.global = global;
        this.globalDists = globalDists;
        this.toMatch = toMatch;
    }

    // Determine if there are sufficient matches between the specified distances and the ones already in
    // the global scanner
    public boolean matchOffsets(NavigableMap<Integer, Coord3D> matchDists) {
        int i = 0;
        LOG.debug("Compare {}", globalDists.keySet());
        LOG.debug("     to {}", matchDists.keySet());
        for (Integer gDist : globalDists.keySet()) {
            for (Integer mDist : matchDists.keySet()) {
                if (gDist.equals(mDist)) {
                    globalPoints[i] = globalDists.get(gDist);
                    matchPoints[i] = matchDists.get(mDist);
                    i++;
                    if (i == REQUIRED_MATCHES) {
                        LOG.debug("Matched {}", toMatch.getId());
                        return true;
                    }
                }
            }
        }

        LOG.debug("Only found {} matches", i);
        return false;
    }

    // Find an orientation that places all points at the same offsets
    public boolean findOrientation() {
        for (Mapper3D map : Mapper3D.values()) {
            // Map the first matched point
            mapMatchPoints[0] = map.map(matchPoints[0]);
            boolean allMatch = true;

            // Map the remaining matched points, checking for the same offset as the first
            for (int i = 1; ((i < REQUIRED_MATCHES) && allMatch); i++) {
                mapMatchPoints[i] = map.map(matchPoints[i]);
                allMatch &= sameOffset(globalPoints[0], globalPoints[i], mapMatchPoints[0], mapMatchPoints[i]);
            }

            // If all the offsets matched, save the orientation
            if (allMatch) {
                matchMapper = map;
                LOG.debug("Orientation is {}", map.name());
                return true;
            }
        }

        return false;
    }

    private boolean sameOffset(Coord3D gblA, Coord3D gblB, Coord3D matA, Coord3D matB) {
        if ((gblB.getX() - gblA.getX()) != (matB.getX() - matA.getX())) {
            return false;
        }
        if ((gblB.getY() - gblA.getY()) != (matB.getY() - matA.getY())) {
            return false;
        }
        if ((gblB.getZ() - gblA.getZ()) != (matB.getZ() - matA.getZ())) {
            return false;
        }
        return true;
    }

    // Check that at least 12 beacons seen by the scanner being matched coincide with global beacons
    public boolean checkMatch() {
        // Position and orient the scanner being matched
        LOG.debug("Global points: {}", Arrays.asList(globalPoints));
        LOG.debug("Match points: {}", Arrays.asList(matchPoints));
        LOG.debug("Mapped points: {}", Arrays.asList(mapMatchPoints));
        toMatch.setOrient(matchMapper);
        toMatch.setLocation(new Coord3D((globalPoints[0].getX() - mapMatchPoints[0].getX()),
                (globalPoints[0].getY() - mapMatchPoints[0].getY()),
                (globalPoints[0].getZ() - mapMatchPoints[0].getZ())));

        // Count the matching beacon locations
        Set<Coord3D> globalBeacons = global.getDists().keySet();
        Set<Coord3D> matchBeacons = toMatch.getBeacons();
        LOG.debug("Matching {}", globalBeacons);
        LOG.debug("      to {}", matchBeacons);
        long count = globalBeacons.stream()
                .filter(c -> matchBeacons.contains(c))
                .count();
        LOG.debug("Found {} matching beacon locations", count);

        return (count > 11);
    }
}
