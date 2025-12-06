package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day13a extends BaseClass<List<Day13Data>> {

    public static void main(String[] args) {
        new Day13a().run(args, "");
    }

    public Day13a() {
        super(false);
        setParser(new ObjectListParser<>(Day13Data::new, Day13Data::add));
    }

    @Override
    public void solve(List<Day13Data> data) {
        Day13Data first = data.get(0);
        int when = first.getWhen();
        List<Integer> ids = first.getIds().stream()
                .filter(i -> i > 0)
                .collect(Collectors.toList());
        LOG.info("read {} ids", ids.size());
        LOG.debug("when = {}, IDs = {}", when, ids);
        Assert.that((ids.size() > 1), "Not enough IDs");

        // Determine how long we would need to wait for each bus
        Map<Integer, Integer> waits = ids.stream()
                .collect(Collectors.toMap(i -> i, i -> i - (when % i)));
        LOG.debug("waits = {}", waits);

        // Find the minimum wait time
        Map.Entry<Integer, Integer> minWait = waits.entrySet().stream()
                .min((a, b) -> a.getValue() - b.getValue())
                .orElseThrow();
        LOG.debug("min wait = {}", minWait);

        LOG.info("answer = {}", (minWait.getKey() * minWait.getValue()));
    }
}
