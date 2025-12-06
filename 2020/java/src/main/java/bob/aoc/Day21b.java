package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.stream.Collectors;

public class Day21b extends BaseClass<List<Day21Food>> {

    public static void main(String[] args) {
        new Day21b().run(args, "");
    }

    public Day21b() {
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

        String ingreds = matcher.getAllergenMap().values().stream()
                .collect(Collectors.joining(","));

        LOG.info("Dangerous ingredients are {}", ingreds);
    }
}
