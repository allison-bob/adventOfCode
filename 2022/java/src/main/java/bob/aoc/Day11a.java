package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day11a extends BaseClass<List<Day11Monkey>> {
    
    Map<Integer, Day11Monkey> mmap;
    
    public static void main(String[] args) {
        new Day11a().run(args, "");
    }

    public Day11a() {
        super(false);
        setParser(new ObjectListParser<>(Day11Monkey::new, Day11Monkey::add));
    }

    @Override
    public void solve(List<Day11Monkey> monkeys) {
        LOG.info("read {} monkeys", monkeys.size());
        monkeys.forEach(m -> LOG.debug(m.toString()));
        mmap = monkeys.stream().collect(Collectors.toMap(Day11Monkey::getId, m -> m));
        
        for (int i = 0; i < 20; i++) {
            doRound(monkeys);
            LOG.debug("After step {}:", (i + 1));
            monkeys.forEach(m -> LOG.debug(m.toString()));
        }
        
        List<Long> counts = monkeys.stream()
                .map(Day11Monkey::getInspectCt)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        LOG.info("Answer is {}", (counts.get(0) * counts.get(1)));
    }
    
    private void doRound(List<Day11Monkey> monkeys) {
        for (Day11Monkey monkey : monkeys) {
            doTurn(monkey);
        }
    }
    
    private void doTurn(Day11Monkey monkey) {
        List<Long> holding = monkey.getHolding();
        while (!holding.isEmpty()) {
            long oldval = holding.remove(0);
            long newval = monkey.inspect(oldval, true);
            int throwTo = monkey.throwTo(newval);
            mmap.get(throwTo).getHolding().add(newval);
        }
    }
}
