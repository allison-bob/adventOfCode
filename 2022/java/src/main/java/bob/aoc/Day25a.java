package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day25a extends BaseClass<List<String>> {

    public static void main(String[] args) {
        new Day25a().run(args, "");
    }

    public Day25a() {
        super(false);
        setParser(new ObjectParser<>(line -> line));
    }

    @Override
    public void solve(List<String> numbers) {
        LOG.info("Read {} numbers", numbers.size());

        // Parse and add up the numbers read
        long sum = 0;
        for (String n : numbers) {
            long value = 0;
            for (char c : n.toCharArray()) {
                Day25Digit d = Day25Digit.byChar(c);
                value = (5 * value) + d.getValue();
            }
            LOG.debug("{} translates to {}", n, value);
            sum += value;
        }
        LOG.debug("Sum is {}", sum);

        // Build SNAFU number
        StringBuilder sb = new StringBuilder();
        while (sum > 0) {
            long s = sum + 2;
            int rem = (int) (s % 5);
            Day25Digit d = Day25Digit.byValue(rem - 2);
            sb.insert(0, d.getSymbol());
            sum = s / 5;
        }

        LOG.info("Answer is {}", sb.toString());
    }
}
