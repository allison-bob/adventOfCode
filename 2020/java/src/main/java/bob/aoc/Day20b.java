package bob.aoc;

import bob.data.coordinate.Mapper2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DMapParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day20b extends BaseClass<Map<Integer, Grid2D<Day20Pixel>>> {

    public static void main(String[] args) {
        new Day20b().run(args, "");
    }

    public Day20b() {
        super(false);
        setParser(new Grid2DMapParser<>(false, false, Day20Pixel::byChar, this::parseID));
    }
    
    private Integer parseID(String line) {
        Integer id = Integer.valueOf(line.substring(5, line.length() - 1));
        Assert.that((id > 0), "Tile number must be >0");
        return id;
    }

    private final Pattern[] monsterPatts = new Pattern[]{
        Pattern.compile("..................#."),
        Pattern.compile("#....##....##....###"),
        Pattern.compile(".#..#..#..#..#..#...")
    };

    @Override
    public void solve(Map<Integer, Grid2D<Day20Pixel>> tiles) {
        LOG.info("read {} tiles", tiles.size());
        LOG.debug("tile IDs: {}", tiles.keySet());
        
        // Validate that all tiles are square and the same size
        int tileSize = 0;
        for (Map.Entry<Integer, Grid2D<Day20Pixel>> e : tiles.entrySet()) {
            if (tileSize == 0) {
                tileSize = e.getValue().getSize().getX();
            }
            Assert.that((e.getValue().getSize().getX() == tileSize),
                    "X size of tile " + e.getKey() + " is not " + tileSize);
            Assert.that((e.getValue().getSize().getY() == tileSize),
                    "Y size of tile " + e.getKey() + " is not " + tileSize);
        }
        
        Day20EdgeMatcher matcher = new Day20EdgeMatcher(tiles);
        matcher.findMatches();

        // Create a large grid with all points
        Grid2D<Day20Pixel> wholeImage = new Grid2D<>(false, false);
        Grid2D<Integer> tilegrid = matcher.getTilegrid();
        int gridSize = tilegrid.getSize().getX();
        for (int ty = 0; ty < gridSize; ty++) {
            List<Grid2D<Day20Pixel>> tilerow = new ArrayList<>();
            for (int tx = 0; tx < gridSize; tx++) {
                tilerow.add(tiles.get(tilegrid.get(tx, ty)));
            }
            for (int y = 1; y < (tileSize - 1); y++) {
                List<Day20Pixel> bigrow = new ArrayList<>();
                for (int tx = 0; tx < tilerow.size(); tx++) {
                    for (int x = 1; x < (tileSize - 1); x++) {
                        bigrow.add(tilerow.get(tx).get(x, y));
                    }
                }
                wholeImage.addRow(bigrow);
            }
        }

        for (Mapper2D orient : Mapper2D.values()) {
            scanForMonsters(wholeImage, orient);
        }
    }

    private void scanForMonsters(Grid2D<Day20Pixel> wholeImage, Mapper2D orientation) {
        wholeImage.setOrientation(orientation);
        String[] lines = wholeImage.dump(Day20Pixel::getSymbol).split("\n");
        int monsterCt = 0;

        for (int i = 1; i < (lines.length - 1); i++) {
            Matcher m1 = monsterPatts[1].matcher(lines[i]);
            while (m1.find()) {
                // Found the middle line pattern in this line, try the bottom line pattern
                Matcher m2 = monsterPatts[2].matcher(lines[i + 1]);
                if (m2.find(m1.start())) {
                    // Found the bottom line pattern
                    if (m2.start() == m1.start()) {
                        // Bottom line match starts at the same column as the middle line
                        Matcher m0 = monsterPatts[0].matcher(lines[i - 1]);
                        if (m0.find(m1.start())) {
                            // Found top line pattern
                            if (m0.start() == m1.start()) {
                                // Top line match starts at the same column as the middle line
                                LOG.debug("{}: lines from {} match at {}", orientation.name(), (i - 1), m0.start());
                                monsterCt++;
                            }
                        }
                    }
                }
            }
        }
        
        if (monsterCt > 0) {
            long dotct = wholeImage.pointStream()
                    .filter(p -> p == Day20Pixel.WHITE)
                    .count();
            LOG.info("{}: answer = {}", orientation.name(), (dotct - (monsterCt * 15)));
        }
    }
}
