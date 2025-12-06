package bob.aoc;

import bob.algorithm.AStar;
import bob.data.graph.Graph;
import bob.parser.ObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.List;

public class Day23a extends BaseClass<List<String>> {

    public static void main(String[] args) {
        new Day23a().run(args, "");
    }

    public Day23a() {
        super(false);
        setParser(new ObjectParser<>(line -> line));
    }

    @Override
    public void solve(List<String> data) {
        LOG.info("Read {} lines", data.size());
        
        // Build the known states
        Graph<String, Day23State> graph = new Graph<>(String.class, Day23State.class);
        Day23State startState = initialState(data, graph);
        Day23State endState = finalState(2, graph);
        
        // Initialize the graph
        graph.addNode(startState);
        graph.addNode(endState);
        
        // Run the algorithm to find the answer
        AStar<String, Day23State> algo = new AStar<>(graph);
        int energy = algo.findPath(startState.getId(), endState.getId());
        
        LOG.info("Answer is {}", energy);
    }
    
    private Day23State initialState(List<String> data, Graph<String, Day23State> graph) {
        Assert.that((data.size() == 5), "Unexpected number of lines");
        Assert.that(data.get(0).equals("#############"), "Unexpected first line");
        Assert.that(data.get(1).equals("#...........#"), "Unexpected second line");
        Assert.that(data.get(4).equals("  #########"), "Unexpected last line");
        
        Day23Amphipod[] pods = new Day23Amphipod[8];
        pods[0] = new Day23Amphipod(2, 1, data.get(2).charAt(3));
        pods[1] = new Day23Amphipod(4, 1, data.get(2).charAt(5));
        pods[2] = new Day23Amphipod(6, 1, data.get(2).charAt(7));
        pods[3] = new Day23Amphipod(8, 1, data.get(2).charAt(9));
        pods[4] = new Day23Amphipod(2, 2, data.get(3).charAt(3));
        pods[5] = new Day23Amphipod(4, 2, data.get(3).charAt(5));
        pods[6] = new Day23Amphipod(6, 2, data.get(3).charAt(7));
        pods[7] = new Day23Amphipod(8, 2, data.get(3).charAt(9));
        
        return new Day23State(graph, pods);
    }
    
    private Day23State finalState(int lineCt, Graph<String, Day23State> graph) {
        Day23Amphipod[] pods = new Day23Amphipod[4 * lineCt];
        for (int y = 0; y < lineCt; y++) {
            for (Day23AmphipodType t : Day23AmphipodType.values()) {
                pods[(4 * y) + t.ordinal()] = new Day23Amphipod(t.getHomeX(), (y + 1), t.getSymbol());
            }
        }
        
        return new Day23State(graph, pods);
    }
}
