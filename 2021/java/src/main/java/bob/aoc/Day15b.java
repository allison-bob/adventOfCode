package bob.aoc;

import bob.algorithm.AStar;
import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day15b extends BaseClass<Grid2D<Day15Node>> {

    public static void main(String[] args) {
        new Day15b().run(args, "");
    }

    public Day15b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day15Node::new));
    }

    @Override
    public void solve(Grid2D<Day15Node> origdata) {
        LOG.info("Read {} points", origdata.getSize());

        // Expand the grid
        Grid2D<Day15Node> data = expandGrid(origdata);

        // Convert the grid to a graph
        Graph<Coord2D, Day15Node> graph = Day15Common.gridToGraph(data);
        Coord2D size = data.getSize();
        
        // Run the algorithm to find the answer
        AStar<Coord2D, Day15Node> algo = new AStar<>(graph);
        int risk = algo.findPath(new Coord2D(0, 0), new Coord2D((size.getX() - 1), (size.getY() - 1)));
        
        LOG.info("Answer is {}", risk);
    }

    private Grid2D<Day15Node> expandGrid(Grid2D<Day15Node> in) {
        Grid2D<Day15Node> retval = new Grid2D<>();
        Coord2D insize = in.getSize();

        // Collect the input rows to a list
        List<List<Day15Node>> inrows = in.rowStream().collect(Collectors.toList());

        // Expand the row size to 5x
        for (List<Day15Node> r : inrows) {
            for (int i = 1; i < 5; i++) {
                for (int x = 0; x < insize.getX(); x++) {
                    r.add(r.get(x).expandedNode(i));
                }
            }
            retval.addRow(r);
        }

        // Expand the row count to 5x
        for (int i = 1; i < 5; i++) {
            for (List<Day15Node> r : inrows) {
                List<Day15Node> a = new ArrayList<>();
                for (int x = 0; x < r.size(); x++) {
                    a.add(r.get(x).expandedNode(i));
                }
                retval.addRow(a);
            }
        }

        return retval;
    }
}
