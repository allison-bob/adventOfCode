package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.function.Function;

public class Day01b extends BaseClass<List<String>> {

    public static void main(String[] args) {
        new Day01b().run(args, "b");
    }

    public Day01b() {
        super(false);
        setParser(new ObjectParser<>(Function.identity()));
    }

    @Override
    public void solve(List<String> entries) {
        LOG.info("Read {} entries", entries.size());
        
        int answer = entries.stream()
                .map(s -> s.replaceAll("one", "o1e"))
                .map(s -> s.replaceAll("two", "t2o"))
                .map(s -> s.replaceAll("three", "t3e"))
                .map(s -> s.replaceAll("four", "f4r"))
                .map(s -> s.replaceAll("five", "f5e"))
                .map(s -> s.replaceAll("six", "s6x"))
                .map(s -> s.replaceAll("seven", "s7n"))
                .map(s -> s.replaceAll("eight", "e8t"))
                .map(s -> s.replaceAll("nine", "n9e"))
                .map(s -> s.replaceAll("[^0-9]", ""))
                .map(String::getBytes)
                .map(ba -> new byte[] {ba[0], ba[ba.length-1]})
                .map(ba -> new String(ba))
                .mapToInt(Integer::parseInt)
                .sum();
        LOG.info("total: {}", answer);
    }
}
