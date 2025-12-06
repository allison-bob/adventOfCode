package bob.aoc;

import bob.data.coordinate.Mapper2D;
import bob.data.grid.Grid2D;
import bob.util.Assert;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day20EdgeMatcher {

    public static final int TOP = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    
    private final Map<Integer, Grid2D<Day20Pixel>> tiles;
    private final Map<Integer, EnumMap<Mapper2D, String[]>> edgemap = new HashMap<>();
    private final Map<Integer, EnumSet<Mapper2D>> candidates = new HashMap<>();
    private final Map<String, List<Day20EdgeID>> edgeMatches = new HashMap<>();
    private final Map<Integer, int[]> neighbors = new TreeMap<>();
    @Getter
    private final Grid2D<Integer> tilegrid = new Grid2D<>(false, false);
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public Day20EdgeMatcher(Map<Integer, Grid2D<Day20Pixel>> tiles) {
        this.tiles = tiles;
    }

    public void findMatches() {
        // Build the edge map and initial candidate list
        for (Integer tilenum : tiles.keySet()) {
            LOG.debug("Collecting edges for tile {}", tilenum);
            edgemap.put(tilenum, buildAllEdges(tiles.get(tilenum)));

            candidates.put(tilenum, EnumSet.allOf(Mapper2D.class));
        }

        // Match up edges
        findMatch(true);
        while (findMatch(false));

        // Make sure all tiles have been oriented
        for (Map.Entry<Integer, EnumSet<Mapper2D>> e : candidates.entrySet()) {
            Assert.that((e.getValue().size() == 1), "Tile " + e.getKey() + " not oriented");
        }

        // Initialize the neighbor map
        tiles.keySet().forEach(tn -> neighbors.put(tn, new int[4]));

        // Fill in the values
        for (List<Day20EdgeID> edges : edgeMatches.values()) {
            if (edges.size() == 2) {
                Day20EdgeID e0 = edges.get(0);
                Day20EdgeID e1 = edges.get(1);
                neighbors.get(e0.tilenum)[e0.edgenum] = e1.tilenum;
                neighbors.get(e1.tilenum)[e1.edgenum] = e0.tilenum;
            }
        }
        //LOG.debug("neighbors:");
        //neighbors.entrySet().forEach(e -> LOG.debug("{} = {}", e.getKey(), Arrays.toString(e.getValue())));

        // Find the top-left corner
        List<Integer> topleft = neighbors.entrySet().stream()
                .filter(e -> e.getValue()[TOP] == 0)
                .filter(e -> e.getValue()[LEFT] == 0)
                .map(e -> e.getKey())
                .collect(Collectors.toList());
        Assert.that((topleft.size() == 1), "More than one top-left corner");

        // Build the top row of tiles
        List<Integer> row = new ArrayList<>();
        int tilenum = topleft.get(0);
        while (tilenum > 0) {
            row.add(tilenum);
            tilenum = neighbors.get(tilenum)[RIGHT];
        }
        tilegrid.addRow(row);

        // Build the remaining rows
        int rowct = tiles.size() / row.size();
        for (int y = 1; y < rowct; y++) {
            for (int x = 0; x < row.size(); x++) {
                row.set(x, neighbors.get(row.get(x))[BOTTOM]);
            }
            tilegrid.addRow(row);
        }
        LOG.debug("tile locations:\n{}", tilegrid.dump(l -> l.toString() + " "));
    }

    private EnumMap<Mapper2D, String[]> buildAllEdges(Grid2D<Day20Pixel> tile) {
        EnumMap<Mapper2D, String[]> retval = new EnumMap<>(Mapper2D.class);

        for (Mapper2D orient : Mapper2D.values()) {
            tile.setOrientation(orient);
            retval.put(orient, buildEdges(tile));
            //LOG.debug("orient {} edges {}", orient.name(), Arrays.asList(retval.get(orient)));
        }

        tile.setOrientation(Mapper2D.EN);
        return retval;
    }

    private String[] buildEdges(Grid2D<Day20Pixel> tile) {
        String[] retval = new String[4];
        int tileSize = tile.getSize().getX();

        retval[TOP] = IntStream.range(0, tileSize)
                .mapToObj(x -> tile.get(x, 0))
                .map(Day20Pixel::getSymbol)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        retval[BOTTOM] = IntStream.range(0, tileSize)
                .mapToObj(x -> tile.get(x, (tileSize - 1)))
                .map(Day20Pixel::getSymbol)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        retval[LEFT] = IntStream.range(0, tileSize)
                .mapToObj(y -> tile.get(0, y))
                .map(Day20Pixel::getSymbol)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        retval[RIGHT] = IntStream.range(0, tileSize)
                .mapToObj(y -> tile.get((tileSize - 1), y))
                .map(Day20Pixel::getSymbol)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return retval;
    }

    private boolean findMatch(boolean first) {
        edgeMatches.clear();

        // Collect the edge values
        for (int tilenum : tiles.keySet()) {
            for (Mapper2D orient : candidates.get(tilenum)) {
                String[] edges = edgemap.get(tilenum).get(orient);
                for (int i = 0; i < edges.length; i++) {
                    if (!edgeMatches.containsKey(edges[i])) {
                        edgeMatches.put(edges[i], new ArrayList<>());
                    }
                    edgeMatches.get(edges[i]).add(new Day20EdgeID(tilenum, orient, i));
                }
            }
        }

        // Find an edge that can be matched up
        for (String edge : edgeMatches.keySet()) {
            //LOG.debug("Checking edge {}, appearance count is {}", edge, edgeMatches.get(edge).size());
            // If the edge only appears 1 or 2 times, both tiles have been oriented
            if (edgeMatches.get(edge).size() > 2) {
                // If this isn't the first attempt, ensure at least one tile with this edge is oriented
                if (first || (edgeMatches.get(edge).size() < 8)) {
                    // Get the set of tiles with the edge string
                    Map<Integer, List<Day20EdgeID>> edgeTiles = edgeMatches.get(edge).stream()
                            .collect(Collectors.groupingBy(e -> e.tilenum));
                    if (edgeTiles.size() == 2) {
                        // Get the tiles with this edge string
                        Integer[] tilenums = edgeTiles.keySet().toArray(new Integer[2]);

                        if (edgeTiles.get(tilenums[0]).size() == 1) {
                            // First tile is oriented, match up second tile
                            orientTile(edgeTiles.get(tilenums[0]).get(0), edgeTiles.get(tilenums[1]));
                        } else if (edgeTiles.get(tilenums[1]).size() == 1) {
                            // Second tile is oriented, match up first tile
                            orientTile(edgeTiles.get(tilenums[1]).get(0), edgeTiles.get(tilenums[0]));
                        } else {
                            // Neither tile is oriented, select the lowest orientation ordinal for the first tile
                            Day20EdgeID t1sel = edgeTiles.get(tilenums[0]).stream()
                                    .min((e1, e2)
                                            -> Integer.compare(e1.orientation.ordinal(), e2.orientation.ordinal()))
                                    .get();
                            // Orient both tiles
                            orientTile(t1sel, null);
                            orientTile(t1sel, edgeTiles.get(tilenums[1]));
                        }
                        // Reset for another match
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void orientTile(Day20EdgeID selected, List<Day20EdgeID> options) {
        if (options == null) {
            // No options, orient the first tile
            LOG.debug("Setting orientation of {} to {}", selected.tilenum, selected.orientation.name());
            candidates.get(selected.tilenum).clear();
            candidates.get(selected.tilenum).add(selected.orientation);
            tiles.get(selected.tilenum).setOrientation(selected.orientation);
        } else {
            for (Day20EdgeID e : options) {
                if ((selected.edgenum + e.edgenum) == 3) {
                    // Edge matched, orient the matched tile
                    LOG.debug("Setting orientation of {} to {}", e.tilenum, e.orientation.name());
                    candidates.get(e.tilenum).clear();
                    candidates.get(e.tilenum).add(e.orientation);
                    tiles.get(e.tilenum).setOrientation(e.orientation);
                    return;
                }
            }
            throw Assert.failed(null, "No edge in " + options + " matched " + selected);
        }
    }
}
