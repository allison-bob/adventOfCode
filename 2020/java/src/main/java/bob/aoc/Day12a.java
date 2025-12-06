package bob.aoc;

import bob.data.CPU;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day12a extends BaseClass<List<Day12Instr>> {

    public static void main(String[] args) {
        new Day12a().run(args, "");
    }

    public Day12a() {
        super(false);
        setParser(new ObjectParser<>(Day12Instr::new));
    }

    private enum OpCode {
        N, S, E, W, L, R, F;
    };

    @Override
    public void solve(List<Day12Instr> directions) {
        LOG.info("directions contains {} instructions", directions.size());

        CPU<Day12Instr> cpu = new CPU<>(directions, this::showStep, this::showResult);
        long[] reg = cpu.getReg();
        reg[0] = 0;
        reg[1] = 0;
        reg[2] = 1;
        reg[3] = 0;
        reg[4] = 0;

        cpu.run();

        // What is the Manhattan distance the turtle travelled?
        LOG.info("Manhattan distance is {}", (Math.abs(reg[0]) + Math.abs(reg[1])));
    }
    
    private boolean showStep(CPU<Day12Instr> cpu) {
        int pc = cpu.getPc();
        List<Day12Instr> program = cpu.getProgram();
        LOG.debug("pc {}, instruction {}", pc, program.get(pc));
        return true;
    }
    
    private boolean showResult(CPU<Day12Instr> cpu) {
        long[] reg = cpu.getReg();
        LOG.debug("position {},{}, direction {},{}", reg[0], reg[1], reg[2], reg[3]);
        return true;
    }
}
