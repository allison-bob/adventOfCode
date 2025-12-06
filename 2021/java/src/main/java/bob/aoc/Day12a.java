package bob.aoc;

import bob.data.graph.Graph;
import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day12a extends BaseClass<List<Graph<String, Day12Cave>>> {

    public static void main(String[] args) {
        new Day12a().run(args, "");
    }

    public Day12a() {
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
                List<Day12Cave> toTest = new ArrayList<>(currpath);
                toTest.add(n);
                if (n.getId().equals("end")) {
                    completePaths.add(toTest);
                } else {
                    if (n.isLarge() || (!currpath.contains(n))) {
                        pathsToCheck.add(toTest);
                    }
                }
            }
        }

        LOG.info("Total number of paths is " + completePaths.size());
    }
}
