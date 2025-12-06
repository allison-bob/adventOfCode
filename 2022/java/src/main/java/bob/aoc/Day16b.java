package bob.aoc;

import bob.data.graph.Graph;
import bob.parser.LineObjectMapParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Map;

public class Day16b extends BaseClass<Map<String, Day16Valve>> {

    public static void main(String[] args) {
        new Day16b().run(args, "");
    }

    public Day16b() {
        super(false);
        setParser(new LineObjectMapParser<>(Day16Valve::new, Day16Valve::getId));
    }

    @Override
    public void solve(Map<String, Day16Valve> valves) {
        LOG.info("Read {} valves", valves.size());

        // Build the graph
        Graph<String, Day16Valve> graph = new Graph<>(String.class, Day16Valve.class);
        for (Day16Valve v : valves.values()) {
            graph.addNode(v);
            for (String t : v.getTunnelsTo()) {
                graph.addEdge(v.getId(), t, 0);
            }
        }
        LOG.info("Constructed {} nodes and {} edges", graph.getNodeCt(), graph.getEdgeCt());
        
        Day16FloydWarshall algo = new Day16FloydWarshall(graph, graph.get("AA"), 26);
        int answer = algo.findMaxCost(new Day16State(graph.get("AA"), 26, new ArrayList<>(), 1));
        
        LOG.info("Answer is {}", answer);
    }
}
