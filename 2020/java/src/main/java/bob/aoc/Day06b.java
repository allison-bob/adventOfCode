package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day06b extends BaseClass<List<Day06Group>> {

    public static void main(String[] args) {
        new Day06b().run(args, "");
    }

    public Day06b() {
        super(false);
        setParser(new ObjectListParser<>(Day06Group::new, Day06Group::addAnswers));
    }

    @Override
    public void solve(List<Day06Group> groups) {
        LOG.info("read {} groups", groups.size());

        int count = groups.stream()
                .mapToInt(this::count)
                .sum();

        LOG.info("Total of all counts is {}", count);
    }

    private int count(Day06Group grp) {
        Set<Character> allyes = grp.getAnswers().stream()
                .flatMap(a -> a.getYes().stream())
                .collect(Collectors.toSet());

        for (Day06Answers a : grp.getAnswers()) {
            allyes.retainAll(a.getYes());
        }

        return allyes.size();
    }
}
