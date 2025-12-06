package bob.aoc;

import bob.algorithm.FloydWarshall;
import bob.data.graph.Graph;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day16FloydWarshall extends FloydWarshall<String, Day16Valve, Day16State> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final Day16Valve start;
    private final int maxTime;

    public Day16FloydWarshall(Graph<String, Day16Valve> graph, Day16Valve start, int maxTime) {
        super(graph, Day16FloydWarshall::newState);
        this.start = start;
        this.maxTime = maxTime;
    }

    @Override
    protected Integer isDone(Day16State state) {
        // If time has not elapsed, continue this exploration
        if (state.minute() > 0) {
            return null;
        }

        // If we don't need to explore another elephant, cost from here is 0
        if (state.addedElephants() == 0) {
            return 0;
        }

        // Run an exploration from this point with another elephant
        return findCost(new Day16State(start, maxTime, state.opened(), (state.addedElephants() - 1)));
    }
    
    private static Day16State newState(Day16State state, Day16Valve newStart) {
        return new Day16State(newStart, (state.minute() - 1), state.opened(), state.addedElephants());
    }

    @Override
    protected int visit(Day16State state) {
        LOG.debug("Visiting {}", state);
        
        // If this valve can't be opened, no need to process this valve
        if (state.valve().getFlowRate() <= 0) {
            return worstRoute;
        }

        // If this valve is already open on this path, no need to process this valve
        if (state.opened().contains(state.valve())) {
            return worstRoute;
        }

        // Open the valve
        state.opened().add(state.valve());
        // Make sure the opened valves are sorted, to make the cache work
        Collections.sort(state.opened());
        // Find the best route with this valve open
        int cost = ((state.minute() - 1) * state.valve().getFlowRate())
                + findCost(new Day16State(state.valve(), (state.minute() - 1), state.opened(), state.addedElephants()));
        // Close the valve for other paths to resolve
        state.opened().remove(state.valve());
        
        return cost;
    }
}
