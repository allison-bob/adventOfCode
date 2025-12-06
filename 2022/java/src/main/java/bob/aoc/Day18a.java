package bob.aoc;

import bob.data.coordinate.Coord3D;
import bob.parser.ObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.Arrays;
import java.util.List;

public class Day18a extends BaseClass<List<Coord3D>> {
    
    private static final List<Coord3D> DIRECTIONS = Arrays.asList(
            new Coord3D(-1, 0, 0), new Coord3D(1, 0, 0),
            new Coord3D(0, -1, 0), new Coord3D(0, 1, 0),
            new Coord3D(0, 0, -1), new Coord3D(0, 0, 1)
    );

    public static void main(String[] args) {
        new Day18a().run(args, "");
    }

    public Day18a() {
        super(false);
        setParser(new ObjectParser<>(this::parseLine));
    }
    
    private Coord3D parseLine(String line) {
        String[] bits = line.split(",");
        Assert.that((bits.length == 3), "Wrong number of coordinates");
        return new Coord3D(Integer.parseInt(bits[0]), Integer.parseInt(bits[1]), Integer.parseInt(bits[2]));
    }

    @Override
    public void solve(List<Coord3D> cubes) {
        LOG.info("read {} cubes", cubes.size());
        
        long area = 0;
        for (Coord3D checking : cubes) {
            area += DIRECTIONS.stream()
                    .map(c -> new Coord3D((c.getX() + checking.getX()), (c.getY() + checking.getY()),
                            (c.getZ() + checking.getZ())))
                    .filter(c -> (!cubes.contains(c)))
                    .count();
        }

        LOG.info("Total area is {}", area);
    }
}
