package bob.aoc;

import bob.data.CPU;
import java.util.List;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Day08Execution {

    private final CPU<Day08Instr> cpu;
    private final boolean[] executed;
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public Day08Execution(List<Day08Instr> program) {
        this.cpu = new CPU<>(program, this::prestep, null);
        this.executed = new boolean[program.size()];
    }

    public void run() {
        cpu.run();
    }

    public boolean prestep(CPU cpu) {
        if (executed[cpu.getPc()]) {
            return false;
        }
        LOG.debug("Executing {}, pc={}, accum={}", cpu.getProgram().get(cpu.getPc()), cpu.getPc(), cpu.getReg()[0]);
        executed[cpu.getPc()] = true;
        return true;
    }
}
