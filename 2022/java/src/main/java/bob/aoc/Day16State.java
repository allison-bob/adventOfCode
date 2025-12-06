package bob.aoc;

import bob.algorithm.FloydWarshallState;
import java.util.List;

public record Day16State(Day16Valve valve, int minute, List<Day16Valve> opened, int addedElephants)
        implements FloydWarshallState<String> {

    @Override
    public String getCurrentNode() {
        return valve.getId();
    }
}
