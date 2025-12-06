package bob.aoc;

import bob.util.Assert;
import lombok.Getter;

@Getter
public class Day21Monkey {
    
    private enum Op {
        ADD, SUB, MULT, DIV;
    }

    private String id;
    // Monkey has a job
    private String opA;
    private Op op;
    private String opB;
    // Monkey has a number, possibly by completing their job
    private Long result;

    public Day21Monkey(String line) {
        int pos = line.indexOf(": ");
        Assert.that((pos > 0), "No colon found");
        id = line.substring(0, pos);
        String[] bits = line.substring(pos + 2).split(" ");
        switch (bits.length) {
            case 1 -> result = Long.valueOf(bits[0]);
            case 3 -> {
                opA = bits[0];
                opB = bits[2];
                op = switch (bits[1]) {
                    case "+" -> Op.ADD;
                    case "-" -> Op.SUB;
                    case "*" -> Op.MULT;
                    case "/" -> Op.DIV;
                    default -> throw Assert.failed(null, "Unexpected operation character");
                };
            }
            default -> throw Assert.failed(null, "parse failure");
        }
    }
    
    public void compute(long a, long b) {
        result = switch (op) {
            case ADD -> a + b;
            case SUB -> a - b;
            case MULT -> a * b;
            case DIV -> a / b;
        };
    }
    
    public long computeA(long result, long b) {
        return switch (op) {
            case ADD -> result - b;
            case SUB -> result + b;
            case MULT -> result / b;
            case DIV -> result * b;
        };
    }
    
    public long computeB(long result, long a) {
        return switch (op) {
            case ADD -> result - a;
            case SUB -> a - result;
            case MULT -> result / a;
            case DIV -> a / result;
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(id);
        if (op != null) {
            sb.append(" (").append(opA).append(" ").append(op.name()).append(" ").append(opB).append(")");
        }
        if (result != null) {
            sb.append(": ").append(result);
        }
        return sb.toString();
    }
}
