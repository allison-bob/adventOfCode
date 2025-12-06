package bob.aoc;

import bob.data.CPU;
import lombok.Getter;

@Getter
public class Day10Instr implements CPU.Operation {

    public enum OpCode {
        ADDX, NOOP;
    }

    private final OpCode op;
    private final int value;

    public Day10Instr(String line) {
        String[] bits = line.split(" ");
        op = OpCode.valueOf(bits[0].toUpperCase());
        if (op == OpCode.ADDX) {
            value = Integer.parseInt(bits[1]);
        } else {
            value = 0;
        }
    }

    @Override
    public void execute(CPU cpu) {
        // Registers:
        //   0 -- The X value after the current instruction
        //   1 -- The cycle completed after the current instruction
        //   2 -- The X value before the current instruction
        switch (op) {
            case ADDX -> {
                cpu.getReg()[2] = cpu.getReg()[0];
                cpu.getReg()[0] += value;
                cpu.getReg()[1] += 2;
                cpu.setPc(cpu.getPc() + 1);
            }
            case NOOP -> {
                cpu.getReg()[2] = cpu.getReg()[0];
                cpu.getReg()[1] += 1;
                cpu.setPc(cpu.getPc() + 1);
            }
            default ->
                throw new IllegalArgumentException("Unknown op " + op);
        }
    }

    @Override
    public String toString() {
        return op + ": " + value;
    }
}
