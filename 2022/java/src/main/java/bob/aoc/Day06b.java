package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.Arrays;
import java.util.List;

public class Day06b extends BaseClass<List<String>> {

    public static void main(String[] args) {
        new Day06b().run(args, "");
    }

    public Day06b() {
        super(false);
        setParser(new ObjectParser<>(line -> line));
    }

    @Override
    public void solve(List<String> data) {
        for (String buffer : data) {
            for (int i = 0; i < buffer.length(); i++) {
                char[] bits = buffer.substring(i, (i + 14)).toCharArray();
                Arrays.sort(bits);
                boolean found = true;
                for (int j = 0; ((j < (bits.length - 1)) && (found)); j++) {
                    found &= (bits[j] != bits[j + 1]);
                }
                if (found) {
                    LOG.info("Packet starts at {}", (i + 14));
                    break;
                }
            }
        }
    }
}
