package bob.aoc;

import bob.algorithm.DepthFirstSearch;
import bob.data.graph.Graph;
import bob.parser.LineObjectMapParser;
import bob.util.BaseClass;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class Day19b extends BaseClass<Map<Integer, Day19Blueprint>> {

    private Graph<UUID, Day19Node> graph;
    private Day19Blueprint blueprint;

    public static void main(String[] args) {
        new Day19b().run(args, "");
    }

    public Day19b() {
        super(false);
        setParser(new LineObjectMapParser<>(Day19Blueprint::new, Day19Blueprint::getId));
    }

    @Override
    public void solve(Map<Integer, Day19Blueprint> blueprints) {
        LOG.info("Read {} blueprints", blueprints.size());

        long answer = 1;
        for (int i = 1; i < (isRealData() ? 4 : 3); i++) {
            // Build an initial graph for this blueprint
            blueprint = blueprints.get(i);
            graph = new Graph<>(UUID.class, Day19Node.class);
            Day19Node startPoint = new Day19Node(32);
            graph.addNode(startPoint);

            // Find best result for this blueprint
            DepthFirstSearch<UUID, Day19Node> algo = new DepthFirstSearch<>(graph, this::visit, this::isDone);
            Day19Node result = algo.findMaxCost(startPoint);
            LOG.debug("\n\n\n\n");
            LOG.info("Blueprint {} yielded {}", i, result.getCostToHere());
            LOG.debug("\n\n\n\n");

            // Add to the answer
            answer *= result.getCostToHere();
        }

        LOG.info("Answer is {}", answer);
    }

    private boolean isDone(Day19Node node) {
        return node.getDepth() >= 32;
    }

    public void visit(Day19Node node) {
        // Find out what can be built
        boolean[] canBuild = blueprint.canBuild(node.getHoldings(), node.getRobotCt());
        LOG.debug("... can build {}", Arrays.toString(canBuild));

        /*
         Optimizations derived from megathread comments (none of these are bad choices to avoid,
         but don't contribute to reaching the desired goal so not producing the potential step
         reduces the number of steps to evaluate):
          -- If this path cannot yield a better result then the best so far, don't add any nodes
             (this is implemented in the algorithm)
          -- If a robot could have been built in the last step but wasn't, there's no reason
             to build the robot in this step
          -- Do not build more robots of a type than needed to build something else
          -- if depth == 32, there is no need to build any robots
          -- If depth == 31, the only useful robot to build is geode
          -- If depth == 30, there is no reason to build a clay robot
        
         */
        // Build a geode robot
        if (canBuild[3] && (!node.getNotBuilt()[3]) && (node.getDepth() < 32)) {
            addToGraph(node, new Day19Node(node, "geode", blueprint));
        }

        // Build an obsidian robot
        if (canBuild[2] && (!node.getNotBuilt()[2]) && (node.getDepth() < 31)) {
            addToGraph(node, new Day19Node(node, "obsidian", blueprint));
        }

        // Build a clay robot
        if (canBuild[1] && (!node.getNotBuilt()[1]) && (node.getDepth() < 30)) {
            addToGraph(node, new Day19Node(node, "clay", blueprint));
        }

        // Build an ore robot
        if (canBuild[0] && (!node.getNotBuilt()[0]) && (node.getDepth() < 31)) {
            addToGraph(node, new Day19Node(node, "ore", blueprint));
        }

        // Add a node that builds nothing
        if (blueprint.notTooMuch(node.getHoldings())) {
            addToGraph(node, new Day19Node(node, canBuild));
        }
    }

    private void addToGraph(Day19Node node, Day19Node newnode) {
//        LOG.debug("Adding {}", newnode);
        graph.addNode(newnode);
        graph.addEdge(node.getId(), newnode.getId(), 0);
    }
}
