package bob.aoc;

import bob.data.CPU;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day14b extends BaseClass<List<Day14Instr>> {

    public static void main(String[] args) {
        new Day14b().run(args, "a");
    }

    public Day14b() {
        super(false);
        setParser(new ObjectParser<>(Day14Instr::buildB));
    }

    @Override
    public void solve(List<Day14Instr> program) {
        LOG.info("program contains {} instructions", program.size());

        CPU<Day14Instr> cpu = new CPU<>(program);
        cpu.run();

        // Compute the result
        long sum = cpu.getMem().values().stream().mapToLong(l -> l).sum();
        LOG.info("answer = " + sum);
    }
}
