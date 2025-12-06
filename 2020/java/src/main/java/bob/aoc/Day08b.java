package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day08b extends BaseClass<List<Day08Instr>> {

    public static void main(String[] args) {
        new Day08b().run(args, "");
    }

    public Day08b() {
        super(false);
        setParser(new ObjectParser<>(Day08Instr::new));
    }

    @Override
    public void solve(List<Day08Instr> program) {
        LOG.info("program contains {} instructions", program.size());

        // Add a NOP at the end as starter for fix search
        program.add(new Day08Instr("nop 0"));

        // Run the first time
        Day08Execution failed = new Day08Execution(program);
        failed.run();
        Assert.that((failed.getCpu().getPc() < program.size()), "fail run succeeded");

        // Fix the program
        int toFix = comeFrom(failed, program, program.size() - 1);
        LOG.info("Fix instruction {}: {}", toFix, program.get(toFix));
        Day08Instr instrToFix = (Day08Instr) program.get(toFix);
        if (instrToFix.getOp() == Day08Instr.OpCode.JMP) {
            instrToFix.setOp(Day08Instr.OpCode.NOP);
        } else {
            instrToFix.setOp(Day08Instr.OpCode.JMP);
        }

        // Run again to get the answer
        Day08Execution success = new Day08Execution(program);
        success.run();
        Assert.that((success.getCpu().getPc() >= program.size()), "success run failed ");

        LOG.info("Final accumulator value is {}", success.getCpu().getReg()[0]);
    }

    private int comeFrom(Day08Execution failed, List<Day08Instr> program, int pc) {
        // Start from the specified PC to find an instruction to fix, working backwards to an unexecuted JMP
        for (int toCheck = pc; toCheck > 0; toCheck--) {
            Day08Instr inst = program.get(toCheck);
            LOG.debug("Checking {}: {}", toCheck, inst);
            if ((inst.getOp() == Day08Instr.OpCode.JMP) && failed.getExecuted()[toCheck]) {
                // Found an executed JMP that can be turned into a NOP to fix the program
                return toCheck;
            }
            if ((inst.getOp() == Day08Instr.OpCode.JMP) && ((inst.getValue() > 1) || (inst.getValue() < 0)) && (toCheck != pc)) {
                // A JMP that was not executed means this is a dead end
                return 0;
            }

            // Get the list of places that would point here
            List<Integer> found = findTarget(toCheck, program);
            LOG.debug("      ... found {}", found);
            for (int i : found) {
                Day08Instr target = (Day08Instr) program.get(i);
                LOG.debug("  ... target {}: {}", i, target);
                if ((target.getOp() == Day08Instr.OpCode.NOP) && failed.getExecuted()[i]) {
                    // Found an executed NOP that can be turned into a JMP to fix the program
                    return toCheck;
                }
                if (target.getOp() == Day08Instr.OpCode.JMP) {
                    // Found a JMP that would lead to the instruction to fix, check this
                    int result = comeFrom(failed, program, i);
                    if (result > 0) {
                        return result;
                    }
                }
            }
        }

        // Nothing found
        return 0;
    }

    private List<Integer> findTarget(int pc, List<Day08Instr> program) {
        List<Integer> retval = new ArrayList<>();

        for (int i = 0; i < program.size(); i++) {
            Day08Instr inst = (Day08Instr) program.get(i);
            if (inst.getOp() != Day08Instr.OpCode.ACC) {
                if (((i + inst.getValue()) == pc) && (i != pc)) {
                    LOG.debug("      ... found {}", inst);
                    retval.add(i);
                }
            }
        }

        return retval;
    }
}
