package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.util.BaseClass;
import java.util.Map;

public class Day21a extends BaseClass<Map<String, Day21Monkey>> {

    public static void main(String[] args) {
        new Day21a().run(args, "");
    }

    public Day21a() {
        super(false);
        setParser(new LineObjectMapParser<>(Day21Monkey::new, Day21Monkey::getId));
    }

    @Override
    public void solve(Map<String, Day21Monkey> monkeys) {
        LOG.info("read {} monkeys", monkeys.size());

        boolean resolved = false;
        while (!resolved) {
            resolved = true;
            for (Day21Monkey m : monkeys.values()) {
                if (m.getResult() == null) {
                    if ((monkeys.get(m.getOpA()).getResult() != null)
                            && (monkeys.get(m.getOpB()).getResult() != null)) {
                        m.compute(monkeys.get(m.getOpA()).getResult(), monkeys.get(m.getOpB()).getResult());
                    } else {
                        resolved = false;
                    }
                }
            }
        }

        LOG.info("Answer is {}", monkeys.get("root").getResult());
    }
}
