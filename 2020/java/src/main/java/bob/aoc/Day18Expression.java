package bob.aoc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day18Expression {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final Pattern OP_PATTERN = Pattern.compile("[()*+]");
    @Getter
    private final String line;
    @Getter
    private final List<Day18Instr> operations;

    public Day18Expression(String line, boolean addParens) {
        this.line = line;
        this.operations = new ArrayList<>();

        Deque<Day18Instr> pending = new ArrayDeque<>();

        // Remove spaces in the line
        String min = line.replaceAll(" ", "");

        if (addParens) {
            // Add parens for proper precedence
            min = "((" + min.replaceAll("\\(", "(((")
                    .replaceAll("\\)", ")))")
                    .replaceAll("\\*", "))*((")
                    .replaceAll("\\+", ")+(") + "))";
        }

        // Find all operators as offsets into the expression
        Matcher m = OP_PATTERN.matcher(min);
        List<Integer> ops = m.results()
                .map(mr -> mr.start())
                .collect(Collectors.toList());
        LOG.debug("line = {}, ops at {}", min, ops);

        // Parse through the line
        int pos = 0;
        int opnum = 0;
        while (pos < min.length()) {
            int nextop = (opnum < ops.size()) ? ops.get(opnum) : min.length();
            if (pos < nextop) {
                operations.add(new Day18Instr(Day18Instr.OpCode.PUSH, Long.parseLong(min.substring(pos, nextop))));
                pos = nextop;
            } else {
                Day18Instr.OpCode code = Day18Instr.OpCode.byChar(min.charAt(pos));
                switch (code) {
                    case LEFT ->
                        pending.push(new Day18Instr(Day18Instr.OpCode.LEFT));
                    case RIGHT -> {
                        while (pending.peek().op != Day18Instr.OpCode.LEFT) {
                            operations.add(pending.pop());
                        }
                        pending.pop();
                    }
                    default -> {
                        while ((!pending.isEmpty()) && (pending.peek().op != Day18Instr.OpCode.LEFT)) {
                            operations.add(pending.pop());
                        }
                        pending.push(new Day18Instr(code));
                    }
                }
                pos++;
                opnum++;
            }
        }
        while (!pending.isEmpty()) {
            operations.add(pending.pop());
        }
    }
}
