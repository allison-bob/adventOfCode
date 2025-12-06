package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day11b extends BaseClass<List<Day11Monkey>> {
    
    Map<Integer, Day11Monkey> mmap;
    
    public static void main(String[] args) {
        new Day11b().run(args, "");
    }

    public Day11b() {
        super(false);
        setParser(new ObjectListParser<>(Day11Monkey::new, Day11Monkey::add));
    }

    @Override
    public void solve(List<Day11Monkey> monkeys) {
        LOG.info("read {} monkeys", monkeys.size());
        monkeys.forEach(m -> LOG.debug(m.toString()));
        mmap = monkeys.stream().collect(Collectors.toMap(Day11Monkey::getId, m -> m));
        
        long globalModulus = monkeys.stream()
                .mapToLong(Day11Monkey::getModulus)
                .reduce(1L, Math::multiplyExact);
        monkeys.forEach(m -> m.setGlobalModulus(globalModulus));
        
        for (int i = 0; i < 10000; i++) {
            doRound(monkeys);
            LOG.debug("After step {}:", (i + 1));
            monkeys.forEach(m -> LOG.debug(m.toString()));
        }
        
        List<Long> counts = monkeys.stream()
                .map(Day11Monkey::getInspectCt)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        LOG.debug("Inspection counts: {}", counts);
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
            long newval = monkey.inspect(oldval, false);
            int throwTo = monkey.throwTo(newval);
            mmap.get(throwTo).getHolding().add(newval);
        }
    }
}
