package bob.aoc;

import bob.data.coordinate.Coord3D;
import bob.parser.ObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day18b extends BaseClass<List<Coord3D>> {

    private static final List<Coord3D> DIRECTIONS = Arrays.asList(
            new Coord3D(-1, 0, 0), new Coord3D(1, 0, 0),
            new Coord3D(0, -1, 0), new Coord3D(0, 1, 0),
            new Coord3D(0, 0, -1), new Coord3D(0, 0, 1)
    );

    public static void main(String[] args) {
        new Day18b().run(args, "");
    }

    public Day18b() {
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

        // Find the bounds: 0=minX, 1=maxX, 2=minY, 3=maxY, 4=minZ, 5=maxZ
        int[] bounds = findBounds(cubes);
        LOG.debug("Bounds: x:{}..{} y:{}..{} z:{}..{}",
                bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);

        // Find the cubes that can touch water
        Set<Coord3D> water = new HashSet<>();
        findWater(water, cubes, bounds, new Coord3D(bounds[0], bounds[2], bounds[4]));

        Set<Coord3D> allcubes = new TreeSet<>();
        allcubes.addAll(cubes);
        LOG.debug("Input cubes: {}", allcubes);
        allcubes.addAll(water);
        LOG.debug("All cubes: {}", allcubes);
        
        
        long area = 0;
        for (Coord3D checking : cubes) {
            area += DIRECTIONS.stream()
                    .map(c -> new Coord3D((c.getX() + checking.getX()), (c.getY() + checking.getY()),
                    (c.getZ() + checking.getZ())))
                    .filter(c -> (water.contains(c)))
                    .count();
            LOG.debug("After {}, total is {}", checking, area);
        }

        LOG.info("Total area is {}", area);
    }

    private int[] findBounds(List<Coord3D> cubes) {
        int[] bounds = new int[6];
        bounds[0] = cubes.stream()
                .mapToInt(Coord3D::getX)
                .min().getAsInt();
        bounds[1] = cubes.stream()
                .mapToInt(Coord3D::getX)
                .max().getAsInt();
        bounds[2] = cubes.stream()
                .mapToInt(Coord3D::getY)
                .min().getAsInt();
        bounds[3] = cubes.stream()
                .mapToInt(Coord3D::getY)
                .max().getAsInt();
        bounds[4] = cubes.stream()
                .mapToInt(Coord3D::getZ)
                .min().getAsInt();
        bounds[5] = cubes.stream()
                .mapToInt(Coord3D::getZ)
                .max().getAsInt();
        return bounds;
    }

    private void findWater(Set<Coord3D> water, List<Coord3D> cubes, int[] bounds, Coord3D start) {
        // If way outside the bounds, add nothing
        if ((start.getX() < (bounds[0] - 1)) || (start.getX() > (bounds[1] + 1))
         || (start.getY() < (bounds[2] - 1)) || (start.getY() > (bounds[3] + 1))
         || (start.getZ() < (bounds[4] - 1)) || (start.getZ() > (bounds[5] + 1))) {
            return;
        }

        water.add(start);

        List<Coord3D> found = DIRECTIONS.stream()
                .map(c -> new Coord3D((c.getX() + start.getX()), (c.getY() + start.getY()),
                (c.getZ() + start.getZ())))
                .filter(c -> (!cubes.contains(c)))
                .filter(c -> (!water.contains(c)))
                .collect(Collectors.toList());
        LOG.debug("From {}, found {}", start, found);

        for (Coord3D w : found) {
            findWater(water, cubes, bounds, w);
            LOG.debug("Set now {}", water.size());
        }
    }
}
