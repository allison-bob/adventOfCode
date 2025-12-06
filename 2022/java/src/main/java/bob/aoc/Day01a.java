package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day01a extends BaseClass<List<List<Integer>>> {

    public static void main(String[] args) {
        new Day01a().run(args, "");
    }

    public Day01a() {
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
                .mapToInt(il -> il.stream()
                .mapToInt(v -> v)
                .sum()
                )
                .max().getAsInt();

        LOG.info("Max sum is {}", max);
    }
}
