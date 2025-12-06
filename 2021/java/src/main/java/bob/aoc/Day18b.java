package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.io.ByteArrayInputStream;
import java.util.List;

public class Day18b extends BaseClass<List<Day18SnailFishNumber>> {

    public static void main(String[] args) {
        new Day18b().run(args, "");
    }

    public Day18b() {
        super(false);
        setParser(new ObjectParser<>(line -> new Day18SnailFishNumber(new ByteArrayInputStream(line.getBytes()))));
    }

    @Override
    public void solve(List<Day18SnailFishNumber> numbers) {
        LOG.info("Read {} snailfish numbers", numbers.size());

        long result = 0;
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = 0; j < numbers.size(); j++) {
                if (i != j) {
                    result = Math.max(result, magOfSum(numbers.get(i), numbers.get(j)));
                }
            }
        }
        
        LOG.info("Result magnitude is {}", result);
    }

    private long magOfSum(Day18SnailFishNumber num1, Day18SnailFishNumber num2) {
        LOG.debug("Summing {} + {}", num1, num2);
        Day18SnailFishNumber sum = new Day18SnailFishNumber(num1, num2);
        sum.reduce();
        
        long result = sum.magnitude();
        LOG.debug(result + " <-- " + sum + "\n");
        return result;
    }
}
