package bob.aoc;

import bob.parser.LineListParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.function.BinaryOperator;

public class Day07b extends BaseClass<List<List<Long>>> {

    private enum Operator {
        PLUS((a, b) -> a + b),
        TIMES((a, b) -> a * b),
        CONCAT((a,b) -> Long.valueOf(a.toString() + b.toString()));

        BinaryOperator<Long> op;

        private Operator(BinaryOperator<Long> op) {
            this.op = op;
        }

        public Long operate(Long a, Long b) {
            return op.apply(a, b);
        }
    }

    public static void main(String[] args) {
        new Day07b().run(args, "");
    }

    public Day07b() {
        super(false);
        setParser(new LineListParser<>("[: ]", Long::valueOf));
    }

    @Override
    public void solve(List<List<Long>> data) {
        LOG.info("Read {} lines", data.size());

        long total = 0;
        for (List<Long> d : data) {
            LOG.debug("Line {}", d);
            List<Long> stack = d.subList(2, d.size());
            if (compute(d.get(0), d.get(1), stack)) {
                total += d.get(0);
            }
            LOG.debug("Total is now {}", total);
        }

        LOG.info("Total is {}", total);
    }

    private boolean compute(long desired, long currval, List<Long> stack) {
        LOG.debug("... compute({}, {}, {})", desired, currval, stack);
        
        // Are we done?
        if (stack.isEmpty()) {
            return desired == currval;
        }
        
        // Try each operator
        for (Operator op : Operator.values()) {
            // Update the current value using the operator
            long newval = op.operate(currval, stack.get(0));
            // Process the next element
            if (compute(desired, newval, stack.subList(1, stack.size()))) {
                return true;
            }
        }
        
        // Nothing worked
        return false;
    }
}
