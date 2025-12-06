package bob.aoc;

import bob.data.CPU;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day18b extends BaseClass<List<Day18Expression>> {

    public static void main(String[] args) {
        new Day18b().run(args, "");
    }

    public Day18b() {
        super(false);
        setParser(new ObjectParser<>(line -> new Day18Expression(line, true)));
    }

    @Override
    public void solve(List<Day18Expression> expressions) {
        LOG.info("read {} expressions", expressions.size());

        long sum = 0;
        for (Day18Expression e : expressions) {
            LOG.debug("evaluating {}", e.getLine());
            CPU<Day18Instr> cpu = new CPU<>(e.getOperations());
            cpu.run();
            LOG.debug("... evaluates to {}", cpu.stackPeek());
            sum += cpu.stackPeek();
        }
        
        // Compute the result
        LOG.info("answer = {}", sum);
    }
}
