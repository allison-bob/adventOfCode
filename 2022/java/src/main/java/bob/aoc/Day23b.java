package bob.aoc;

import bob.data.coordinate.Coord2D;
import bob.data.grid.Grid2D;
import bob.parser.Grid2DParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day23b extends BaseClass<Grid2D<Day23Spot>> {

    private Grid2D<Day23Spot> map;
    private int firstDir = 0;
    private Day23Direction[] dirs = Day23Direction.values();

    public static void main(String[] args) {
        new Day23b().run(args, "");
    }

    public Day23b() {
        super(false);
        setParser(new Grid2DParser<>(false, false, Day23Spot::byChar));
    }

    @Override
    public void solve(Grid2D<Day23Spot> data) {
        map = data;
        LOG.info("read {} grid", map.getSize());

        boolean stable = false;
        int i = 0;
        for (i = 0; ((i < 10000) && (!stable)); i++) {
            stable = doRound();
            firstDir++;
            LOG.debug("Grid after {} moves:\n{}", (i + 1), map.dump(Day23Spot::getSymbol));
        }
        
        LOG.info("No moves after {} rounds", i);
    }

    private boolean doRound() {
        // Add a border if there's an elf on the edge
        map.addBorderIf(Day23Spot.EMPTY);

        // First half, collect proposals
        List<Day23Proposal> proposals = new ArrayList<>();
        for (int y = 1; y < (map.getSize().getY() - 1); y++) {
            for (int x = 1; x < (map.getSize().getX() - 1); x++) {
                if (map.get(x, y) == Day23Spot.ELF) {
                    Coord2D s = new Coord2D(x, y);
                    Coord2D p = getProposal(s);
                    if (p != null) {
                        proposals.add(new Day23Proposal(s, p));
                    }
                }
            }
        }
        LOG.debug("Proposals: {}", proposals);
        if (proposals.isEmpty()) {
            return true;
        }

        // Find duplicate proposals
        Map<Coord2D, List<Day23Proposal>> destmap = proposals.stream()
                .collect(Collectors.groupingBy(Day23Proposal::dest));

        // Second half, make unconflicted moves
        boolean stable = true;
        for (List<Day23Proposal> proplist : destmap.values()) {
            if (proplist.size() == 1) {
                Day23Proposal prop = proplist.get(0);
                Assert.that((map.get(prop.start()) == Day23Spot.ELF), "Starting on empty spot");
                Assert.that((map.get(prop.dest()) == Day23Spot.EMPTY), "Moving to another elf");
                map.set(prop.start(), Day23Spot.EMPTY);
                map.set(prop.dest(), Day23Spot.ELF);
                stable = false;
            }
        }
        return stable;
    }

    private Coord2D findMinSize() {
        int rows = map.getSize().getX();
        int cols = map.getSize().getY();

        boolean foundElf = false;
        for (int y = 0; ((y < map.getSize().getY()) && (!foundElf)); y++) {
            for (int x = 0; ((x < map.getSize().getX()) && (!foundElf)); x++) {
                foundElf = (map.get(x, y) == Day23Spot.ELF);
            }
            if (!foundElf) {
                rows--;
            }
        }

        foundElf = false;
        for (int y = (map.getSize().getY() - 1); ((y > 0) && (!foundElf)); y--) {
            for (int x = 0; ((x < map.getSize().getX()) && (!foundElf)); x++) {
                foundElf = (map.get(x, y) == Day23Spot.ELF);
            }
            if (!foundElf) {
                rows--;
            }
        }

        foundElf = false;
        for (int x = 0; ((x < map.getSize().getX()) && (!foundElf)); x++) {
            for (int y = 0; ((y < map.getSize().getY()) && (!foundElf)); y++) {
                foundElf = (map.get(x, y) == Day23Spot.ELF);
            }
            if (!foundElf) {
                cols--;
            }
        }

        foundElf = false;
        for (int x = (map.getSize().getX() - 1); ((x > 0) && (!foundElf)); x--) {
            for (int y = 0; ((y < map.getSize().getY()) && (!foundElf)); y++) {
                foundElf = (map.get(x, y) == Day23Spot.ELF);
            }
            if (!foundElf) {
                cols--;
            }
        }

        return new Coord2D(cols, rows);
    }

    private Coord2D getProposal(Coord2D start) {
        if (map.neighborStream(start).allMatch(p -> p == Day23Spot.EMPTY)) {
            // All neighbors are empty, no move needed
            return null;
        }

        // Check each direction for a move possibility
        int x = start.getX();
        int y = start.getY();
        for (int i = 0; i < dirs.length; i++) {
            Day23Direction toTest = dirs[(firstDir + i) % dirs.length];
            boolean empty = true;
            for (Coord2D c : toTest.getChecks()) {
                empty &= map.get((x + c.getX()), (y + c.getY())) == Day23Spot.EMPTY;
            }
            if (empty) {
                return new Coord2D((x + toTest.getProposal().getX()), (y + toTest.getProposal().getY()));
            }
        }

        // No move possible
        return null;
    }
}
