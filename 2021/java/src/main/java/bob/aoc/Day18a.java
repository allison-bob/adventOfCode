package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.io.ByteArrayInputStream;
import java.util.List;

public class Day18a extends BaseClass<List<Day18SnailFishNumber>> {

    public static void main(String[] args) {
        new Day18a().run(args, "");
    }

    public Day18a() {
        super(false);
        setParser(new ObjectParser<>(line -> new Day18SnailFishNumber(new ByteArrayInputStream(line.getBytes()))));
    }

    @Override
    public void solve(List<Day18SnailFishNumber> numbers) {
        LOG.info("Read {} snailfish numbers", numbers.size());
        Day18SnailFishNumber result = null;
        for (Day18SnailFishNumber sfn : numbers) {
            LOG.debug(">>{}", sfn);
            if (result == null) {
                // First number, just keep it
                result = sfn;
            } else {
                // Subsequent number, build sum and reduce
                Day18SnailFishNumber sum = new Day18SnailFishNumber(result, sfn);
                LOG.debug("sum is {}", sum);
                sum.reduce();
                LOG.debug("--> reduced to {}", sum);
                result = sum;
            }
        }
        
        LOG.info("Result magnitude is {}", result.magnitude());
    }
}
