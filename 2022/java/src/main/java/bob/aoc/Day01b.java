package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day01b extends BaseClass<List<List<Integer>>> {

    public static void main(String[] args) {
        new Day01b().run(args, "");
    }

    public Day01b() {
        super(false);
        setParser(new ObjectListParser<>(ArrayList::new, this::addItem));
    }

    private void addItem(List<Integer> list, String line) {
        list.add(Integer.valueOf(line));
    }

    @Override
    public void solve(List<List<Integer>> entries) {
        LOG.info("Read {} entries", entries.size());

        int max = entries.stream()
                .map(il -> il.stream()
                .mapToInt(v -> v)
                .sum()
                )
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToInt(v -> v)
                .sum();

        LOG.info("Max sum is {}", max);
    }
}
