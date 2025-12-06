package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day21b extends BaseClass<Map<String, Day21Monkey>> {

    public static void main(String[] args) {
        new Day21b().run(args, "");
    }

    public Day21b() {
        super(false);
        setParser(new LineObjectMapParser<>(Day21Monkey::new, Day21Monkey::getId));
    }

    @Override
    public void solve(Map<String, Day21Monkey> monkeys) {
        LOG.info("read {} monkeys", monkeys.size());

        List<String> unresolved = new ArrayList<>();
        unresolved.add("humn");
        boolean resolved = false;
        while (!resolved) {
            resolved = true;
            for (Day21Monkey m : monkeys.values()) {
                if (!unresolved.contains(m.getId())) {
                    if (m.getResult() == null) {
                        if (unresolved.contains(m.getOpA()) || unresolved.contains(m.getOpB())) {
                            unresolved.add(m.getId());
                        } else {
                            if ((monkeys.get(m.getOpA()).getResult() != null)
                                    && (monkeys.get(m.getOpB()).getResult() != null)) {
                                m.compute(monkeys.get(m.getOpA()).getResult(), monkeys.get(m.getOpB()).getResult());
                            } else {
                                resolved = false;
                            }
                        }
                    }
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Monkeys:");
            monkeys.keySet().stream()
                    .sorted()
                    .forEach(id -> {
                        Day21Monkey m = monkeys.get(id);
                        String which = "";
                        if (unresolved.contains(m.getOpA())) {
                            which += " <A>";
                        }
                        if (unresolved.contains(m.getOpB())) {
                            which += " <B>";
                        }
                        LOG.debug("   {} {}", m, which);
                    });
        }

        // The result of this examination is that there is not a place where both operands are unresolved
        Long target;
        Day21Monkey toResolve = monkeys.get("root");
        LOG.debug("Starting point: {}", toResolve);
        if (unresolved.contains(toResolve.getOpA())) {
            target = monkeys.get(toResolve.getOpB()).getResult();
            toResolve = monkeys.get(toResolve.getOpA());
        } else {
            target = monkeys.get(toResolve.getOpA()).getResult();
            toResolve = monkeys.get(toResolve.getOpB());
        }
        while (!toResolve.getId().equals("humn")) {
            LOG.debug("Resolving {} with target {}, ops {}, {}", toResolve, target, monkeys.get(toResolve.getOpA()),
                    monkeys.get(toResolve.getOpB()));
            if (unresolved.contains(toResolve.getOpA())) {
                target = toResolve.computeA(target, monkeys.get(toResolve.getOpB()).getResult());
                toResolve = monkeys.get(toResolve.getOpA());
            } else {
                target = toResolve.computeB(target, monkeys.get(toResolve.getOpA()).getResult());
                toResolve = monkeys.get(toResolve.getOpB());
            }
        }

        LOG.info("Answer is {}", target);
    }
}
