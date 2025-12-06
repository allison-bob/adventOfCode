package bob.aoc;

import bob.data.CPU;
import bob.util.Assert;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Day08Instr implements CPU.Operation {

public enum OpCode {
    ACC, JMP, NOP
}

    @Setter
    private OpCode op;
    private final int value;

    public Day08Instr(String line) {
        String[] bits = line.split(" ");
        Assert.that((bits.length == 2), "Input word count");
        op = OpCode.valueOf(bits[0].toUpperCase());
        value = Integer.parseInt(bits[1]);
    }

    @Override
    public void execute(CPU cpu) {
        switch (op) {
            case ACC -> {
                cpu.getReg()[0] += value;
                cpu.setPc(cpu.getPc() + 1);
            }
            case JMP ->
                cpu.setPc(cpu.getPc() + value);
            case NOP ->
                cpu.setPc(cpu.getPc() + 1);
            default ->
                throw new IllegalArgumentException("Unknown op " + op);
        }
    }

    @Override
    public String toString() {
        return op + ": " + value;
    }
}
