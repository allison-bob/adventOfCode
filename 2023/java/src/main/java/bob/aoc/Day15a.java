package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.Arrays;
import java.util.List;

public class Day15a extends BaseClass<List<List<String>>> {

    public static void main(String[] args) {
        new Day15a().run(args, "");
    }

    public Day15a() {
        super(false);
        setParser(new ObjectParser<>(this::parseLine));
    }
    
    private List<String> parseLine(String line) {
        String[] bits = line.split(",");
        return Arrays.asList(bits);
    }

    @Override
    public void solve(List<List<String>> data) {
        List<String> entries = data.get(0);
        LOG.info("Read {} entries", entries.size());
        
        long answer = entries.stream()
                .mapToLong(this::hash)
                .sum();
        LOG.info("total: {}", answer);
    }
    
    public long hash(String in) {
        int out = 0;
        for (char c : in.toCharArray()) {
            out += c;
            out *= 17;
            out = out % 256;
        }
        return out;
    }
}
