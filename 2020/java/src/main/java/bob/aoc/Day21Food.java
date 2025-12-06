package bob.aoc;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Day21Food {

    private final List<String> ingreds = new ArrayList<>();
    private final List<String> allergens = new ArrayList<>();

    public Day21Food(String line) {
        String[] bits = line.split("[ (),]+");

        // Drop the words into the appropriate places
        List<String> dest = ingreds;
        for (String s : bits) {
            if (s.equals("contains")) {
                dest = allergens;
            } else {
                dest.add(s);
            }
        }
    }

    @Override
    public String toString() {
        return "{I=" + ingreds + ",A=" + allergens + "}";
    }
}
