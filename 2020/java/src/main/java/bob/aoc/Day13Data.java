package bob.aoc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public class Day13Data {

    private int when;
    private List<Integer> ids;

    public void add(String line) {
        if (when == 0) {
            when = Integer.parseInt(line);
        } else {
            ids = Stream.of(line.split(","))
                    .map(id -> (id.equals("x") ? "0" : id))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
    }
}
