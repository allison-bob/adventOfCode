package bob.aoc;

import bob.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day21Matcher {
    
    private final List<Day21Food> foods;
    @Getter
    private final Map<String, String> allergenMap = new TreeMap<>();
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public Day21Matcher(List<Day21Food> foods) {
        this.foods = foods;
    }

    public void match() {
        // Make a list of all allergens
        List<String> allergenList = foods.stream()
                .flatMap(f -> f.getAllergens().stream())
                .distinct()
                .collect(Collectors.toList());

        while (!allergenList.isEmpty()) {
            // Scan each allergen to find a unique ingredient
            List<String> found = new ArrayList<>();
            for (String allergen : allergenList) {
                // Find the list of foods to check
                List<Day21Food> foodlist = foods.stream()
                        .filter(f -> f.getAllergens().contains(allergen))
                        .collect(Collectors.toList());
                LOG.debug("Allergen {} appears in {}", allergen, foodlist);
                Assert.that((!foodlist.isEmpty()), "Allergen " + allergen + " not present anywhere");

                // Find the list of ingredients common to all foods in the list
                List<String> commonIngreds = foodlist.stream()
                        .map(f -> f.getIngreds())
                        .reduce(new ArrayList<>(foodlist.get(0).getIngreds()), (a, b) -> {
                            a.retainAll(b);
                            return a;
                        });
                LOG.debug("Common ingredients are {}", commonIngreds);

                if (commonIngreds.size() == 1) {
                    // Found the ingredient containing that allergen
                    LOG.debug("Allergen {} is in {}", allergen, commonIngreds.get(0));
                    allergenMap.put(allergen, commonIngreds.get(0));
                    // Remove the allergen and ingredient from all foods
                    for (Day21Food f : foods) {
                        f.getIngreds().remove(commonIngreds.get(0));
                        f.getAllergens().remove(allergen);
                        found.add(allergen);
                    }
                }
            }
            Assert.that((!found.isEmpty()), "No allergens could be mapped");
            allergenList.removeAll(found);
            LOG.debug("Remaining allergens: {}", allergenList);
            LOG.debug("Food list now has {}", foods);
        }
    }
}
