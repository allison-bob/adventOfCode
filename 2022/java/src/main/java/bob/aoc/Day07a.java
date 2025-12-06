package bob.aoc;

import bob.data.graph.Graph;
import bob.parser.GraphParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day07a extends BaseClass<Graph<String, Day07Node>> {

    private List<String> treePtr = new ArrayList<>();
    private String loadPtr;
    private Map<String, Long> sizes = new HashMap<>();
    
    public static void main(String[] args) {
        new Day07a().run(args, "");
    }

    public Day07a() {
        super(false);
        setParser(new GraphParser<>(String.class, Day07Node.class, this::parseLine));
    }
    
    private void parseLine(Graph<String, Day07Node> graph, String line) {
        if (line.startsWith("$ cd ")) {
            // Update the current directory
            updatePtrs(line.substring(5));
            if (loadPtr.equals("")) {
                // Need to add the root directory, otherwise, the directory should already exist
                graph.addNode(new Day07Node("/", 0));
            }
        } else if (line.equals("$ ls")) {
            // No need to do anything with this line
        } else if (line.startsWith("dir ")) {
            // A directory in the current directory
            String from = loadPtr.isBlank() ? "/" : loadPtr;
            String to = loadPtr + "/" + line.substring(4);
            graph.addNode(new Day07Node(to, -1));
            graph.addEdge(from, to, 0);
        } else {
            // A file in the current directory
            String[] bits = line.split(" ");
            String from = loadPtr.isBlank() ? "/" : loadPtr;
            String to = loadPtr + "/" + bits[1];
            graph.addNode(new Day07Node(to, Integer.parseInt(bits[0])));
            graph.addEdge(from, to, 0);
        }
    }
    
    private void updatePtrs(String dest) {
        switch (dest) {
            case "/" -> treePtr.clear();
            case ".." -> treePtr.remove(treePtr.size() - 1);
            default -> treePtr.add(dest);
        }
        
        loadPtr = treePtr.stream().collect(Collectors.joining("/", "/", ""));
        if (loadPtr.equals("/")) {
            loadPtr = "";
        }
    }
    
    @Override
    public void solve(Graph<String, Day07Node> data) {
        LOG.info("Read {} nodes and {} edges", data.getNodeCt(), data.getEdgeCt());
        
        // Scan the data to build directory sizes
        totalSize(data, "/");
        
        // Find the answer
        long sum = sizes.values().stream()
                .mapToLong(v -> v)
                .filter(v -> v <= 100000)
                .sum();
        LOG.info("Answer is {}", sum);
    }
    
    private void totalSize(Graph<String, Day07Node> data, String root) {
        long sum = 0;
        List<Day07Node> neighbors = data.getNeighbors(root);
        for (Day07Node n : neighbors) {
            if (n.getSize() < 0) {
                totalSize(data, n.getId());
                sum += sizes.get(n.getId());
            } else {
                sum += n.getSize();
            }
        }
        sizes.put(root, sum);
    }
}
