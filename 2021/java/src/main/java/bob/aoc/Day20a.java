package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.parser.LineObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day20a extends BaseClass<TwoFormatParser.Output<List<List<Day20Pixel>>, Grid2D<Day20Pixel>>> {

    public static void main(String[] args) {
        new Day20a().run(args, "");
    }

    public Day20a() {
        super(false);
        setParser(new TwoFormatParser<>(
                new LineObjectParser<>(Day20Pixel::byChar),
                new Grid2DParser<>(false, false, Day20Pixel::byChar)
        ));
    }

    @Override
    public void solve(TwoFormatParser.Output<List<List<Day20Pixel>>, Grid2D<Day20Pixel>> data) {
        Assert.that((data.first.size() == 1), "More than one algorithm line read");
        List<Day20Pixel> algorithm = data.first.get(0);
        Assert.that((algorithm.size() == 512), "Wrong algorithm length");
        Grid2D<Day20Pixel> image = data.rest;
        LOG.info("Starting image size is {}", image.getSize());
        LOG.debug("Starting image\n{}", image.dump(Day20Pixel::getSymbol));

        Day20Pixel border = Day20Pixel.OFF;
        for (int i = 0; i < 2; i++) {
            Grid2D<Day20Pixel> newimage = process(image, algorithm, border);
            LOG.debug("After step {}\n{}", (i + 1), newimage.dump(Day20Pixel::getSymbol));

            image = newimage;
            border = switch (border) {
                case OFF ->
                    algorithm.get(0);
                case ON ->
                    algorithm.get(511);
            };
        }
        
        long count = image.pointStream()
                .filter(p -> p == Day20Pixel.ON)
                .count();
        LOG.info("There are " + count + " lit pixels");
    }

    private Grid2D<Day20Pixel> process(Grid2D<Day20Pixel> image, List<Day20Pixel> algorithm, Day20Pixel border) {
        Grid2D<Day20Pixel> newimage = new Grid2D<>();
        // Add two borders to ensure the image can grow on each processing
        image.addBorder(border);
        image.addBorder(border);
        Coord2D size = image.getSize();

        for (int y = 1; y < (size.getY() - 1); y++) {
            List<Day20Pixel> newrow = new ArrayList<>();
            for (int x = 1; x < (size.getX() - 1); x++) {
                newrow.add(algorithm.get(indexAt(image, x, y)));
            }
            newimage.addRow(newrow);
        }

        return newimage;
    }

    private int indexAt(Grid2D<Day20Pixel> image, int x, int y) {
        return Stream.of(y - 1, y, y + 1)
                .flatMap(y0 -> Stream.of(new Coord2D(x - 1, y0), new Coord2D(x, y0), new Coord2D(x + 1, y0)))
                .map(image::get)
                .mapToInt(Day20Pixel::getValue)
                .reduce(0, (a, b) -> ((a << 1) + b));
    }
}
