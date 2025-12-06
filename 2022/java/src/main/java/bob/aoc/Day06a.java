package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.Arrays;
import java.util.List;

public class Day06a extends BaseClass<List<String>> {

    public static void main(String[] args) {
        new Day06a().run(args, "");
    }

    public Day06a() {
        super(false);
        setParser(new ObjectParser<>(line -> line));
    }

    @Override
    public void solve(List<String> data) {
        for (String buffer : data) {
            for (int i = 0; i < buffer.length(); i++) {
                char[] bits = new char[] {
                    buffer.charAt(i + 0), buffer.charAt(i + 1), buffer.charAt(i + 2), buffer.charAt(i + 3)
                };
                Arrays.sort(bits);
                if ((bits[0] != bits[1]) && (bits[1] != bits[2]) && (bits[2] != bits[3])) {
                    LOG.info("Packet starts at {}", (i + 4));
                    break;
                }
            }
        }
    }
}
