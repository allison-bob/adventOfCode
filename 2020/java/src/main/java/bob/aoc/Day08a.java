package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day08a extends BaseClass<List<Day08Instr>> {

    public static void main(String[] args) {
        new Day08a().run(args, "");
    }

    public Day08a() {
        super(false);
        setParser(new ObjectParser<>(Day08Instr::new));
    }

    @Override
    public void solve(List<Day08Instr> program) {
        LOG.info("program contains {} instructions", program.size());

        Day08Execution e = new Day08Execution(program);
        e.run();

        LOG.info("Final accumulator value is {}", e.getCpu().getReg()[0]);
    }
}
