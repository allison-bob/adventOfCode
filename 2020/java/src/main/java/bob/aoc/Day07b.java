package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.util.BaseClass;
import java.util.Map;

public class Day07b extends BaseClass<Map<String, Day07Bag>> {

    public static void main(String[] args) {
        new Day07b().run(args, "");
    }

    public Day07b() {
        super(false);
        setParser(new LineObjectMapParser<>(Day07Bag::new, Day07Bag::getColor));
    }

    @Override
    public void solve(Map<String, Day07Bag> rules) {
        LOG.info("read {} rules", rules.size());

        LOG.info("Total required bags is {}", (mustContain(rules, "shiny gold") - 1));
    }

    private int mustContain(Map<String, Day07Bag> rules, String color) {
        Day07Bag r = rules.get(color);
        LOG.debug("Bag color {}: {}", color, r);
        int count = 0;
        for (Day07Bag b : r.getContents()) {
            count += (b.getQuantity() * mustContain(rules, b.getColor()));
        }
        LOG.debug("{} requires {} other bags", color, count);
        return count + 1;
    }
}
