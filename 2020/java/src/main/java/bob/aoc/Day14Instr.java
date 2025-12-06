package bob.aoc;

import bob.data.CPU;
import bob.util.Assert;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day14Instr implements CPU.Operation {
    
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private enum OpCode {
        MASK, SET
    };

    private final boolean useA;
    private final OpCode op;
    private final long addr;
    private final long value;
    private final long andmask;
    private final long ormask;
    private final List<Long> floats;

    private Day14Instr(boolean useA, String line) {
        this.useA = useA;
        if (line.startsWith("mask = ")) {
            op = OpCode.MASK;
            String bits = line.substring(7);
            ormask = Long.parseLong(bits.replace('X', '0'), 2);
            if (useA) {
                andmask = Long.parseLong(bits.replace('X', '1'), 2);
                floats = null;
            } else {
                andmask = 0;
                StringBuilder m = new StringBuilder("000000000000000000000000000000000000");
                floats = new ArrayList<>();
                for (int i = 0; i < bits.length(); i++) {
                    if (bits.charAt(i) == 'X') {
                        m.replace(i, (i + 1), "1");
                        floats.add(Long.valueOf(m.toString(), 2));
                        m.replace(i, (i + 1), "0");
                    }
                }
                Assert.that((floats.size() < 15), "Too many floating bits");
            }
            addr = 0;
            value = 0;
        } else if (line.startsWith("mem[")) {
            op = OpCode.SET;
            int pos = line.indexOf("]");
            addr = Long.parseLong(line.substring(4, pos));
            value = Long.parseLong(line.substring(pos + 4));
            andmask = 0;
            ormask = 0;
            floats = null;
        } else {
            throw new IllegalArgumentException("unexpected input: " + line);
        }
    }

    public static Day14Instr buildA(String line) {
        return new Day14Instr(true, line);
    }

    public static Day14Instr buildB(String line) {
        return new Day14Instr(false, line);
    }

    @Override
    public void execute(CPU cpu) {
        long[] reg = cpu.getReg();
        switch (op) {
            case MASK -> {
                if (useA) {
                    reg[0] = andmask;
                    reg[1] = ormask;
                } else {
                    reg[0] = ormask;
                    reg[1] = floats.size() + 2;
                    for (int i = 0; i < floats.size(); i++) {
                        reg[i + 2] = floats.get(i);
                    }
                }
            }
            case SET -> {
                if (useA) {
                    long v = value;
                    v &= reg[0];
                    v |= reg[1];
                    cpu.memWrite(addr, v);
                } else {
                    long a = addr | reg[0];
                    doSet(a, value, 2, cpu);
                }
            }
            default ->
                throw new IllegalArgumentException("Unknown op " + op);
        }
        cpu.setPc(cpu.getPc() + 1);
    }

    private void doSet(long addr, long value, int regnum, CPU cpu) {
        if (regnum == cpu.getReg()[1]) {
            // Last bit flipped, set actual addresses
            LOG.debug("setting addr 0x{} to {}", Long.toHexString(addr), value);
            cpu.memWrite(addr, value);
            return;
        }

        // Flip this bit
        long mask = cpu.getReg()[regnum];
        LOG.debug("addr=0x{}, regnum={}, mask=0x{}", Long.toHexString(addr), regnum, Long.toHexString(mask));
        long a = addr & ~mask;
        doSet(a, value, (regnum + 1), cpu);
        a |= mask;
        doSet(a, value, (regnum + 1), cpu);
    }

    @Override
    public String toString() {
        switch (op) {
            case MASK -> {
                return "MASK: &:" + Long.toHexString(andmask) + ", |:" + Long.toHexString(ormask) + ", f:" + floats;
            }
            case SET -> {
                return "SET: " + addr + "," + value;
            }
            default ->
                throw new IllegalArgumentException("Unknown op " + op);
        }
    }
}
