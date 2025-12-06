package bob.aoc;

import bob.algorithm.AStarNode;
import bob.data.coordinate.Coord2D;
import bob.data.graph.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Day23State implements AStarNode<String> {

    private static final int HALLWAY_LENGTH = 11;
    private static final List<Coord2D> STOP_FORBIDDEN = new ArrayList<>(4);

    static {
        STOP_FORBIDDEN.add(new Coord2D(2, 0));
        STOP_FORBIDDEN.add(new Coord2D(4, 0));
        STOP_FORBIDDEN.add(new Coord2D(6, 0));
        STOP_FORBIDDEN.add(new Coord2D(8, 0));
    }

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final Graph<String, Day23State> graph;
    private final boolean largeSet;
    private final Day23Amphipod[] pods;
    private final Day23Amphipod[][] spots;
    private final String id;
    private final int estCostToGoal;

    @Setter
    private int costToHere = Integer.MAX_VALUE;

    @Setter
    private boolean visited;

    public Day23State(String id) {
        this.graph = null;
        this.largeSet = false;
        this.pods = null;
        this.spots = null;
        this.id = id;
        this.estCostToGoal = 0;
    }

    public Day23State(Graph<String, Day23State> graph, Day23Amphipod[] pods) {
        this.graph = graph;
        this.largeSet = (pods.length > 8);
        this.pods = pods;
        this.spots = new Day23Amphipod[largeSet ? 5 : 3][HALLWAY_LENGTH];
        this.id = Stream.of(pods)
                .map(Day23Amphipod::getId)
                .sorted()
                .collect(Collectors.joining(","));
        this.estCostToGoal = Stream.of(pods)
                .mapToInt(this::estPodCost)
                .sum();

        for (Day23Amphipod p : pods) {
            spots[p.getLocation().getY()][p.getLocation().getX()] = p;
        }
    }

    private int estPodCost(Day23Amphipod pod) {
        int c = 0;
        if (pod.getLocation().getX() != pod.getType().getHomeX()) {
            // Move into hallway
            c += pod.getLocation().getY();
            // Move to correct room
            c += Math.abs(pod.getLocation().getX() - pod.getType().getHomeX());
            // Move into room
            c++;
        } else if (pod.getLocation().getY() == 0) {
            // Move into room
            c++;
        }
        return c * pod.getType().getCostPerStep();
    }

    @Override
    public void visit() {
        LOG.debug("Visiting {}:{}+{}={}", id, costToHere, estCostToGoal, (costToHere + estCostToGoal));
        LOG.debug(toString());

        for (int i = 0; i < pods.length; i++) {
            // Get the list of possible destinations
            boolean canMoveIn = canMoveIn(pods[i]);
            Map<Coord2D, Integer> dests = possibleMoves(pods[i], canMoveIn);
            LOG.debug("Pod {} has {} destinations, {}", pods[i], dests.size(), canMoveIn);

            // Create a new node and edge for each possible move
            for (Coord2D c : dests.keySet()) {
                Day23Amphipod[] newpods = Arrays.copyOf(pods, pods.length);
                newpods[i] = new Day23Amphipod(c.getX(), c.getY(), pods[i].getType().getSymbol());
                if (c.getX() == pods[i].getType().getHomeX()) {
                    newpods[i].setInFinalSpot(true);
                }

                Day23State newstate = new Day23State(graph, newpods);
                if (graph.get(newstate.id) == null) {
                    LOG.debug("   Adding state {}", newstate.id);
                    graph.addNode(newstate);
                }
                graph.addEdge(id, newstate.id, (dests.get(c) * pods[i].getType().getCostPerStep()));
            }
        }
    }

    private boolean canMoveIn(Day23Amphipod pod) {
        boolean retval = true;
        int x = pod.getType().getHomeX();

        for (int i = 1; i < spots.length; i++) {
            // Still OK if the spot is empty or occupied by the right type of pod
            retval &= ((spots[i][x] == null) || (spots[i][x].getType() == pod.getType()));
        }

        return retval;
    }

    private Map<Coord2D, Integer> possibleMoves(Day23Amphipod pod, boolean canMoveIn) {
        Map<Coord2D, Integer> result = new HashMap<>();

        // Is this pod already positioned?
        if (pod.isInFinalSpot()) {
            return result;
        }

        // Grab current location
        int x = pod.getLocation().getX();
        int y = pod.getLocation().getY();
        int steps = 0;

        // Does the pod need to move into the hall?
        for (int i = y; i > 1; i--) {
            // Check all spots below the first one to make sure this pod can move
            if (y >= i) {
                if (spots[i - 1][x] != null) {
                    // Spot above is occupied, can't move
                    return result;
                }
                y = i - 1;
                steps++;
            }
        }
        // Moves into the hallway are allowed, generate all possibilities
        if (y == 1) {
            if (spots[0][x] != null) {
                // In first room spot and hallway occupied (should never be true)
                return result;
            }
            y = 0;
            steps++;
            result.put(new Coord2D(x, y), steps);

            // Move up and down the hallway
            for (int i = x - 1; ((i >= 0) && (spots[0][i] == null)); i--) {
                result.put(new Coord2D(i, 0), (steps + (x - i)));
            }
            for (int i = x + 1; ((i < HALLWAY_LENGTH) && (spots[0][i] == null)); i++) {
                result.put(new Coord2D(i, 0), (steps + (i - x)));
            }
        }

        // Move into room
        if (canMoveIn) {
            boolean moveAllowed = true;
            int homeX = pod.getType().getHomeX();
            for (int i = Math.min(homeX, (x + 1)); i <= Math.max(homeX, (x - 1)); i++) {
                moveAllowed &= (spots[0][i] == null);
            }
            if (moveAllowed) {
                int y0 = 0;
                for (int i = 1; i < spots.length; i++) {
                    if (spots[i][homeX] == null) {
                        y0 = i;
                    }
                }
                result.put(new Coord2D(homeX, y0), (steps + Math.abs(homeX - x) + y0));
            }
        }

        // Remove the forbidden stops
        for (Coord2D p : STOP_FORBIDDEN) {
            result.remove(p);
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder[] lines = new StringBuilder[]{
            new StringBuilder("...........    cost: ").append(costToHere).append("\n"),
            new StringBuilder("  . . . .\n"),
            new StringBuilder("  . . . .       est: ").append(estCostToGoal).append("\n"),
            new StringBuilder("  . . . .\n"),
            new StringBuilder("  . . . .")
        };
        for (Day23Amphipod pod : pods) {
            lines[pod.getLocation().getY()].setCharAt(pod.getLocation().getX(), pod.getType().getSymbol());
        }
        String result = id + "\n" + lines[0].toString() + lines[1].toString() + lines[2].toString();
        if (largeSet) {
            result += lines[3].toString() + lines[4].toString();
        }
        return result;
    }
}
