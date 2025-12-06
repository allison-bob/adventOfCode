package bob.aoc;

import bob.util.Assert;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class Day19Blueprint {

    private final int id;
    private final Map<String, Integer> oreCost = new HashMap<>();
    private final Map<String, Integer> clayCost = new HashMap<>();
    private final Map<String, Integer> obsidianCost = new HashMap<>();
    private final Map<String, Integer> geodeCost = new HashMap<>();
    private final int maxOreCost;

    public Day19Blueprint(String line) {
        Assert.that((line.startsWith("Blueprint ")), "Wrong prefix");
        int colon = line.indexOf(": ");
        Assert.that((colon > 10), "No colon");
        id = Integer.parseInt(line.substring(10, colon));

        String[] costs = line.substring(colon + 2).split("[.] ");
        Assert.that((costs.length == 4), "Wrong number of cost items");
        Assert.that(costs[0].startsWith("Each ore robot costs "), "First cost not ore");
        singleCost(oreCost, costs[0].substring(21));
        Assert.that(costs[1].startsWith("Each clay robot costs "), "Second cost not clay");
        singleCost(clayCost, costs[1].substring(22));
        Assert.that(costs[2].startsWith("Each obsidian robot costs "), "Third cost not obsidian");
        doubleCost(obsidianCost, costs[2].substring(26));
        Assert.that(costs[3].startsWith("Each geode robot costs "), "Last cost not geode");
        doubleCost(geodeCost, costs[3].substring(23));

        maxOreCost = Math.max(Math.max(oreCost.get("ore"), clayCost.get("ore")),
                Math.max(obsidianCost.get("ore"), geodeCost.get("ore")));
    }

    private void singleCost(Map<String, Integer> dest, String cost) {
        String[] bits = cost.replaceAll("[,.]", "").split(" ");
        dest.put(bits[1], Integer.valueOf(bits[0]));
    }

    private void doubleCost(Map<String, Integer> dest, String cost) {
        int and = cost.indexOf(" and ");
        Assert.that((and > 0), "No and in double cost");
        singleCost(dest, cost.substring(0, and));
        singleCost(dest, cost.substring(and + 5));
    }

    public boolean[] canBuild(int[] holdings, int[] robotCt) {
        boolean[] retval = new boolean[4];
        retval[0] = (robotCt[0] < maxOreCost) && (holdings[0] >= oreCost.get("ore"));
        retval[1] = (robotCt[1] < obsidianCost.get("clay")) && (holdings[0] >= clayCost.get("ore"));
        retval[2] = (robotCt[2] < geodeCost.get("obsidian")) && (holdings[0] >= obsidianCost.get("ore"))
                && (holdings[1] >= obsidianCost.get("clay"));
        retval[3] = (holdings[0] >= geodeCost.get("ore")) && (holdings[2] >= geodeCost.get("obsidian"));
        return retval;
    }

    public boolean notTooMuch(int[] holdings) {
        if (holdings[0] < maxOreCost) {
            return true;
        }
        if (holdings[1] < obsidianCost.get("clay")) {
            return true;
        }
        return (holdings[2] < geodeCost.get("obsidian"));
    }

    @Override
    public String toString() {
        return "" + id + ":ore=" + oreCost + ",clay=" + clayCost + ",obs=" + obsidianCost + ",geo=" + geodeCost;
    }
}
