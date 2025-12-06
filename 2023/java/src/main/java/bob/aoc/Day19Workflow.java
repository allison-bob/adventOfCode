package bob.aoc;

import bob.util.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.LongBinaryOperator;
import java.util.stream.Stream;
import lombok.ToString;

@ToString
public class Day19Workflow {

    @ToString
    public static class Rule {

        public final char category;
        public final BiPredicate<Long, Long> comparator;
        public final long value;
        public final String destination;

        public Rule(String line) {
            String[] bits = line.split(":");
            if (bits.length > 1) {
                category = bits[0].charAt(0);
                comparator = switch (bits[0].charAt(1)) {
                    case '<' -> (a, b) -> a < b;
                    case '>' -> (a, b) -> a > b;
                    default -> (a, b) -> true;
                };
                value = Long.parseLong(bits[0].substring(2));
                destination = bits[1];
            } else {
                category = '~';
                comparator = null;
                value = -1;
                destination = line;
            }
        }

        public String process(Day19Part part) {
            if (category == '~') {
                return destination;
            }
            long val = part.get(category);
            if (comparator.test(val, value)) {
                return destination;
            }
            return null;
        }
    }

    public final String name;
    public final List<Rule> rules;

    public Day19Workflow(String line) {
        String[] bits = line.split("[{},]");
        name = bits[0];
        rules = Stream.of(bits)
                .skip(1)
                .map(Rule::new)
                .toList();
    }

    public String process(Day19Part part) {
        for (Rule rule : rules) {
            String result = rule.process(part);
            if (result != null) {
                return result;
            }
        }
        return "noMatch";
    }
}
