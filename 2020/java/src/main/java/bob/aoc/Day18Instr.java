package bob.aoc;

import bob.data.CPU;

public class Day18Instr implements CPU.Operation {

    public enum OpCode {
        ADD('+'),
        MULT('*'),
        LEFT('('),
        RIGHT(')'),
        PUSH('?');

        public char symbol;

        private OpCode(char symbol) {
            this.symbol = symbol;
        }

        public static OpCode byChar(char in) {
            for (OpCode at : OpCode.values()) {
                if (at.symbol == in) {
                    return at;
                }
            }
            throw new IllegalArgumentException("Unknown OpCode symbol: " + in);
        }
    }

    public OpCode op;
    public long value;

    public Day18Instr() {
    }

    public Day18Instr(OpCode op) {
        this.op = op;
    }

    public Day18Instr(OpCode op, long value) {
        this.op = op;
        this.value = value;
    }

    @Override
    public void execute(CPU cpu) {
        switch (op) {
            case ADD ->
                cpu.stackPush(cpu.stackPop() + cpu.stackPop());
            case MULT ->
                cpu.stackPush(cpu.stackPop() * cpu.stackPop());
            case PUSH ->
                cpu.stackPush(value);
            default ->
                throw new IllegalArgumentException("Illegal op " + op);
        }
        cpu.setPc(cpu.getPc() + 1);
        //LOG.debug("ran " + toString() + ", stack is " + cpu.stack);
    }

    @Override
    public String toString() {
        return op.name() + ":" + value;
    }
}
