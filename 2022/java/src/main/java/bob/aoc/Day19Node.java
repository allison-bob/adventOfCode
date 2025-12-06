package bob.aoc;

import bob.algorithm.DepthFirstSearchNode;
import java.util.Arrays;
import java.util.UUID;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Day19Node implements DepthFirstSearchNode<UUID> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final UUID id;
    private final int maxDepth;
    private final int depth;
    // Array index: 0=ore, 1=clay, 2=obsidian, 3=geode
    private final boolean[] notBuilt;
    private final int[] robotCt;
    private final int[] holdings;

    // Required for graph
    public Day19Node(UUID id) {
        this.id = id;
        this.maxDepth = 0;
        this.depth = 0;
        this.notBuilt = new boolean[]{false, false, false, false};
        this.robotCt = new int[]{0, 0, 0, 0};
        this.holdings = new int[]{0, 0, 0, 0};
    }

    // Initial node
    public Day19Node(int maxDepth) {
        this.id = UUID.randomUUID();
        this.maxDepth = maxDepth;
        this.depth = 0;
        this.notBuilt = new boolean[]{false, false, false, false};
        this.robotCt = new int[]{1, 0, 0, 0};
        this.holdings = new int[]{0, 0, 0, 0};
    }

    // A node from another node where a robot is built
    public Day19Node(Day19Node src, String built, Day19Blueprint blueprint) {
        this.id = UUID.randomUUID();
        this.maxDepth = src.maxDepth;
        this.depth = src.depth + 1;
        this.notBuilt = new boolean[]{false, false, false, false};
        this.robotCt = Arrays.copyOf(src.robotCt, src.robotCt.length);
        this.holdings = Arrays.copyOf(src.holdings, src.holdings.length);
        stepUpdate(built, blueprint);
    }

    // A node from another node where no robot is built
    public Day19Node(Day19Node src, boolean[] notBuilt) {
        this.id = UUID.randomUUID();
        this.maxDepth = src.maxDepth;
        this.depth = src.depth + 1;
        this.notBuilt = notBuilt;
        this.robotCt = Arrays.copyOf(src.robotCt, src.robotCt.length);
        this.holdings = Arrays.copyOf(src.holdings, src.holdings.length);
        stepUpdate("", null);
    }

    private void stepUpdate(String built, Day19Blueprint blueprint) {
        for (int i = 0; i < holdings.length; i++) {
            holdings[i] += robotCt[i];
        }

        switch (built) {
            case "ore" -> {
                robotCt[0]++;
                holdings[0] -= blueprint.getOreCost().get("ore");
            }
            case "clay" -> {
                robotCt[1]++;
                holdings[0] -= blueprint.getClayCost().get("ore");
            }
            case "obsidian" -> {
                robotCt[2]++;
                holdings[0] -= blueprint.getObsidianCost().get("ore");
                holdings[1] -= blueprint.getObsidianCost().get("clay");
            }
            case "geode" -> {
                robotCt[3]++;
                holdings[0] -= blueprint.getGeodeCost().get("ore");
                holdings[2] -= blueprint.getGeodeCost().get("obsidian");
            }
        }
    }

    @Override
    public int getCostToHere() {
        return holdings[3];
    }

    @Override
    public int getEstCostToGoal() {
        int minRemain = maxDepth - depth;
        int fromExisting = minRemain * robotCt[3];
        int fromNew = (minRemain * (minRemain - 1)) / 2;
        return fromExisting + fromNew;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.depth;
        hash = 83 * hash + Arrays.hashCode(this.robotCt);
        hash = 83 * hash + Arrays.hashCode(this.holdings);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Day19Node other = (Day19Node) obj;
        if (this.depth != other.depth) {
            return false;
        }
        if (!Arrays.equals(this.robotCt, other.robotCt)) {
            return false;
        }
        return Arrays.equals(this.holdings, other.holdings);
    }

    @Override
    public String toString() {
        return "{" + id + ":" + depth + "," + Arrays.toString(notBuilt) + "," + Arrays.toString(robotCt)
                + "," + Arrays.toString(holdings) + "}";
    }
}
