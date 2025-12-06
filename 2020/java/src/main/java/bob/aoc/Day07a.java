package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.util.BaseClass;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day07a extends BaseClass<Map<String, Day07Bag>> {

    public static void main(String[] args) {
        new Day07a().run(args, "");
    }

    public Day07a() {
        super(false);
        setParser(new LineObjectMapParser<>(Day07Bag::new, Day07Bag::getColor));
    }

    @Override
    public void solve(Map<String, Day07Bag> rules) {
        LOG.info("read {} rules", rules.size());

        Set<String> found = new HashSet<>();
        found.add("shiny gold");
        Set<String> options = new HashSet<>();
        while (!options.containsAll(found)) {
            options.addAll(found);
            found = rules.values().stream()
                    .filter(r -> r.getContents().stream().anyMatch(b -> options.contains(b.getColor())))
                    .map(r -> r.getColor())
                    .collect(Collectors.toSet());
            LOG.debug("test={}, options={}, found={}", options.containsAll(found), options, found);
        }

        options.remove("shiny gold");
        LOG.info("Total options is {}", options.size());
    }
}
