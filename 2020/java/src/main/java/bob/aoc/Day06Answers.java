package bob.aoc;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public class Day06Answers {

    @Getter
    private final Set<Character> yes = new HashSet<>();

    public Day06Answers(String line) {
        for (char c : line.toCharArray()) {
            yes.add(c);
        }
    }
}
