package bob.aoc;

import bob.data.graph.Node;
import lombok.Getter;

@Getter
public class Day07Node implements Node<String> {
    
    private final String name;
    private final long size;

    public Day07Node(String name) {
        this.name = name;
        this.size = 0;
    }

    public Day07Node(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public String toString() {
        return name + "=" + size;
    }
}
