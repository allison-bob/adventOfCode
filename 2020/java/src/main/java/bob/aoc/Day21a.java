package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day21a extends BaseClass<List<Day21Food>> {

    public static void main(String[] args) {
        new Day21a().run(args, "");
    }

    public Day21a() {
        super(false);
        setParser(new ObjectParser<>(Day21Food::new));
    }

    @Override
    public void solve(List<Day21Food> foods) {
        LOG.info("Read {} foods", foods.size());
        foods.forEach(f -> LOG.debug("read {}", f));
        
        // Match up allergens to ingredients
        Day21Matcher matcher = new Day21Matcher(foods);
        matcher.match();

        long remaining = foods.stream()
                .flatMap(f -> f.getIngreds().stream())
                .count();

        LOG.info("There are {} remaining ingredients", remaining);
    }
}
