package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.parser.ObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day13b extends BaseClass<TwoFormatParser.Output<List<Coord2D>, List<Day13Fold>>> {

    public static void main(String[] args) {
        new Day13b().run(args, "");
    }

    public Day13b() {
        super(false);
        setParser(new TwoFormatParser<>(
                new ObjectParser<>(this::parseLoc),
                new ObjectParser<>(Day13Fold::new)
        ));
    }

    private Coord2D parseLoc(String line) {
        String[] coords = line.split(",");
        Assert.that((coords.length == 2), "Coordinate parse error");
        return new Coord2D(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }

    @Override
    public void solve(TwoFormatParser.Output<List<Coord2D>, List<Day13Fold>> data) {
        Set<Coord2D> dots = new HashSet<>(data.first);
        List<Day13Fold> folds = data.rest;
        LOG.info("read {} dots and {} folds", dots.size(), folds.size());

        // Do all folds
        for (Day13Fold fold : folds) {
            Set<Coord2D> newdots = new HashSet<>();
            for (Coord2D dot : dots) {
                switch (fold.getAxis()) {
                    case X -> {
                        Assert.that((dot.getX() != fold.getLocation()), "Dot on fold");
                        if (dot.getX() > fold.getLocation()) {
                            newdots.add(new Coord2D(newloc(dot.getX(), fold.getLocation()), dot.getY()));
                        } else {
                            newdots.add(dot);
                        }
                    }
                    case Y -> {
                        Assert.that((dot.getY() != fold.getLocation()), "Dot on fold");
                        if (dot.getY() > fold.getLocation()) {
                            newdots.add(new Coord2D(dot.getX(), newloc(dot.getY(), fold.getLocation())));
                        } else {
                            newdots.add(dot);
                        }
                    }
                }
            }
            dots = newdots;
        }
        
        // Find the page dimensions
        int maxX = dots.stream()
                .mapToInt(Coord2D::getX)
                .max().getAsInt();
        int maxY = dots.stream()
                .mapToInt(Coord2D::getY)
                .max().getAsInt();
        
        // Display the page content
        LOG.info("Page content:");
        for (int y = 0; y <= maxY; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x <= maxX; x++) {
                sb.append(' ');
            }
            for (Coord2D dot : dots) {
                if (dot.getY() == y) {
                    sb.replace(dot.getX(), (dot.getX() + 1), "#");
                }
            }
            LOG.info(sb.toString());
        }
    }

    /*
     * New location is the <fold point> - <distance beyond the fold point>
     */
    private int newloc(int currpos, int foldAt) {
        return foldAt - (currpos - foldAt);
    }
}
