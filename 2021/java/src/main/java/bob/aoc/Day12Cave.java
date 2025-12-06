package bob.aoc;

import bob.data.graph.Node;
import lombok.Getter;

@Getter
public class Day12Cave implements Node<String> {

    private final String id;
    private final boolean large;

    public Day12Cave(String id) {
        this.id = id;
        this.large = id.equals(id.toUpperCase());
    }

    @Override
    public String toString() {
        return id;
    }
}
