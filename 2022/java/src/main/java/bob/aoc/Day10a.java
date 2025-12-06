package bob.aoc;

import bob.data.CPU;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day10a extends BaseClass<List<Day10Instr>> {
    
    private long lookFor = 20;
    private long answer = 0;

    public static void main(String[] args) {
        new Day10a().run(args, "");
    }

    public Day10a() {
        super(false);
        setParser(new ObjectParser<>(Day10Instr::new));
    }

    @Override
    public void solve(List<Day10Instr> program) {
        LOG.info("program contains {} instructions", program.size());

        CPU cpu = new CPU<>(program, this::prestep, this::poststep);
        cpu.getReg()[0] = 1;
        cpu.run();

        LOG.info("Answer is {}", answer);
    }

    public boolean prestep(CPU cpu) {
        LOG.debug("Executing {}, reg={},{},{}", cpu.getProgram().get(cpu.getPc()),
                cpu.getReg()[0], cpu.getReg()[1], cpu.getReg()[2]);
        return cpu.getReg()[1] < 220;
    }

    public boolean poststep(CPU cpu) {
        if (cpu.getReg()[1] >= lookFor) {
            answer += (lookFor * cpu.getReg()[2]);
            LOG.debug("During cycle {}, X is {}, answer is now {}", lookFor, cpu.getReg()[2], answer);
            lookFor += 40;
        }
        return true;
    }
}
