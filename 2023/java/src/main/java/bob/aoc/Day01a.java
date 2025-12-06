package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.function.Function;

public class Day01a extends BaseClass<List<String>> {

    public static void main(String[] args) {
        new Day01a().run(args, "");
    }

    public Day01a() {
        super(false);
        setParser(new ObjectParser<>(Function.identity()));
    }

    @Override
    public void solve(List<String> entries) {
        LOG.info("Read {} entries", entries.size());
        
        int answer = entries.stream()
                .map(s -> s.replaceAll("[^0-9]", ""))
                .map(String::getBytes)
                .map(ba -> new byte[] {ba[0], ba[ba.length-1]})
                .map(ba -> new String(ba))
                .mapToInt(Integer::parseInt)
                .sum();
        LOG.info("total: {}", answer);
    }
}
