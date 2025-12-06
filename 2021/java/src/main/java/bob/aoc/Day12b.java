package bob.aoc;

import bob.data.graph.Graph;
import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day12b extends BaseClass<List<Graph<String, Day12Cave>>> {

    public static void main(String[] args) {
        new Day12b().run(args, "");
    }

    public Day12b() {
        super(false);
        setParser(new ObjectListParser<>(
                () -> new Graph<String, Day12Cave>(String.class, Day12Cave.class),
                (g, l) -> g.addEdges(true, l.split("-")))
        );
    }

    @Override
    public void solve(List<Graph<String, Day12Cave>> data) {
        Graph<String, Day12Cave> system = data.get(0);
        LOG.info("read {} edges creating {} nodes", system.getEdgeCt(), system.getNodeCt());

        List<List<Day12Cave>> pathsToCheck = new ArrayList<>();
        List<List<Day12Cave>> completePaths = new ArrayList<>();
        List<Day12Cave> smallCaves = system.nodeStream()
                .filter(c -> !c.isLarge())
                .collect(Collectors.toList());

        // Set the starting point
        List<Day12Cave> currpath = new ArrayList<>();
        currpath.add(system.get("start"));
        pathsToCheck.add(currpath);

        // Build each possible path
        while (!pathsToCheck.isEmpty()) {
            currpath = pathsToCheck.remove(0);
            LOG.debug("Checking path: " + currpath);
            Day12Cave c = currpath.get(currpath.size() - 1);
            List<Day12Cave> neighbors = system.getNeighbors(c.getId());
            LOG.debug("neighbors of " + c + " are " + neighbors);
            for (Day12Cave n : neighbors) {
                if (!n.getId().equals("start")) {
                    List<Day12Cave> toTest = new ArrayList<>(currpath);
                    toTest.add(n);
                    if (n.getId().equals("end")) {
                        completePaths.add(toTest);
                    } else {
                        if (goodPath(currpath, n, smallCaves)) {
                            LOG.debug("... need to check path {}", toTest);
                            pathsToCheck.add(toTest);
                        }
                    }
                }
            }
        }

        LOG.info("Total number of paths is " + completePaths.size());
    }

    private boolean goodPath(List<Day12Cave> currpath, Day12Cave n, List<Day12Cave> smallCaves) {
        if (n.isLarge()) {
            // This is a large cave, we can go there
            return true;
        }
        // At this point, we know we are heading for a small cave
        if (!currpath.contains(n)) {
            // We've not been there so we can go there
            return true;
        }
        for (Day12Cave sc : smallCaves) {
            if (currpath.indexOf(sc) != currpath.lastIndexOf(sc)) {
                // We've already been to a small cave twice
                return false;
            }
        }
        return true;
    }
}
