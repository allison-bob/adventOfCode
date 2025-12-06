package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.EqualsAndHashCode;

public class Day16a extends BaseClass<Grid2D<Day16Spot>> {

    @EqualsAndHashCode
    private static class Beam {

        public Coord2D location;
        public Day16Direction direction;

        public Beam(Coord2D location, Day16Direction direction) {
            this.location = location;
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "(" + location + ":" + direction + ")";
        }
    }

    public static void main(String[] args) {
        new Day16a().run(args, "");
    }

    public Day16a() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day16Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day16Spot> map) {
        LOG.info("read {} grid", map.getSize());

        Set<Beam> beamCache = new HashSet<>();
        List<Beam> beams = new ArrayList<>();
        beams.add(new Beam(new Coord2D(-1, 0), Day16Direction.RIGHT));

        Set<Coord2D> energized = new HashSet<>();

        // Follow each beam until it exits
        LOG.debug("Start: {} -- {}", beams, energized);
        while (!beams.isEmpty()) {
        //for (int i = 0; i < 50; i++) {
            List<Beam> newbeams = new ArrayList<>();
            
            // Step each beam
            for (Beam beam : beams) {
                Coord2D newloc = new Coord2D((beam.location.getX() + beam.direction.getDx()),
                        (beam.location.getY() + beam.direction.getDy()));
                Day16Spot spot = map.get(newloc);
                if (spot != null) {
                    Day16Direction newdir = beam.direction;
                    energized.add(newloc);
                    newbeams.addAll(adjustBeam(newloc, newdir, map.get(newloc)));
                }
            }
            
            // Remove any beams that are cached
            newbeams.removeAll(beamCache);
            
            // Cache all remaining beams
            beamCache.addAll(newbeams);
            
            beams = newbeams;
            LOG.debug("End: {} -- {}", beams, energized);
        }

        LOG.info("Total of {} spots are energized", energized.size());
    }

    private List<Beam> adjustBeam(Coord2D newloc, Day16Direction newdir, Day16Spot spot) {
        return switch (spot) {
            case EMPTY ->
                List.of(new Beam(newloc, newdir));
            case MIRROR_D -> {
                Day16Direction altdir = switch (newdir) {
                    case UP ->
                        Day16Direction.LEFT;
                    case DOWN ->
                        Day16Direction.RIGHT;
                    case LEFT ->
                        Day16Direction.UP;
                    case RIGHT ->
                        Day16Direction.DOWN;
                };
                yield List.of(new Beam(newloc, altdir));
            }
            case MIRROR_U -> {
                Day16Direction altdir = switch (newdir) {
                    case UP ->
                        Day16Direction.RIGHT;
                    case DOWN ->
                        Day16Direction.LEFT;
                    case LEFT ->
                        Day16Direction.DOWN;
                    case RIGHT ->
                        Day16Direction.UP;
                };
                yield List.of(new Beam(newloc, altdir));
            }
            case SPLIT_LR -> {
                if ((newdir == Day16Direction.LEFT) || (newdir == Day16Direction.RIGHT)) {
                    yield List.of(new Beam(newloc, newdir));
                }
                yield List.of(new Beam(newloc, Day16Direction.LEFT), new Beam(newloc, Day16Direction.RIGHT));
            }
            case SPLIT_UD -> {
                if ((newdir == Day16Direction.UP) || (newdir == Day16Direction.DOWN)) {
                    yield List.of(new Beam(newloc, newdir));
                }
                yield List.of(new Beam(newloc, Day16Direction.UP), new Beam(newloc, Day16Direction.DOWN));
            }
        };
    }
}
