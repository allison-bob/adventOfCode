package bob.aoc;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class Day06Group {

    @Getter
    private final List<Day06Answers> answers = new ArrayList<>();

    public void addAnswers(String line) {
        answers.add(new Day06Answers(line));
    }
}
