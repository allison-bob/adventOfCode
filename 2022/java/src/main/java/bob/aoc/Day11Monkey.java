package bob.aoc;

import bob.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Day11Monkey {
    
    private enum Op {
        ADD, MULT;
    }

    private int id;
    private List<Long> holding = new ArrayList<>();
    private Op operation;
    private Integer operand;
    private long modulus;
    private int trueToss;
    private int falseToss;
    private long inspectCt = 0;
    @Setter
    private long globalModulus = 1;
    
    public void add(String line) {
        if (line.startsWith("Monkey ")) {
            int pos = line.indexOf(":");
            id = Integer.parseInt(line.substring(7, pos));
        } else if (line.startsWith("  Starting items: ")) {
            holding.addAll(Stream.of(line.substring(17).split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .collect(Collectors.toList()));
        } else if (line.startsWith("  Operation: ")) {
            switch (line.charAt(23)) {
                case '+' -> operation = Op.ADD;
                case '*' -> operation = Op.MULT;
                default -> throw Assert.failed(null, "Unexpected operation character: " + line.substring(23));
            }
            if (!line.substring(25).equals("old")) {
                operand = Integer.valueOf(line.substring(25));
            }
        } else if (line.startsWith("  Test: ")) {
            modulus = Integer.parseInt(line.substring(21));
        } else if (line.startsWith("    If true: ")) {
            trueToss = Integer.parseInt(line.substring(29));
        } else if (line.startsWith("    If false: ")) {
            falseToss = Integer.parseInt(line.substring(30));
        }
    }
    
    public long inspect(long value, boolean divBy3) {
        inspectCt++;
        long adj = switch (operation) {
            case ADD -> value + ((operand == null) ? value : operand);
            case MULT -> value * ((operand == null) ? value : operand);
        };
        return divBy3 ? (adj / 3) : (adj % globalModulus);
    }
    
    public int throwTo(long value) {
        return ((value % modulus) == 0) ? trueToss : falseToss;
    }

    @Override
    public String toString() {
        return "Monkey(" + id + "," + operation + "," + operand + "," + modulus + "," + trueToss + "," + falseToss
                + " [" + inspectCt + "] holds " + holding + ")";
    }
}
