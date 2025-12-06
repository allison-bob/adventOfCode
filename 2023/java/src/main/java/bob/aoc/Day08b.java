package bob.aoc;

import bob.algorithm.ChineseRemainder;
import bob.parser.LineObjectMapParser;
import bob.parser.LineObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Day08b extends BaseClass<TwoFormatParser.Output<List<List<Day08Direction>>, Map<String, Day08Step>>> {

    private static final int[] PRIMES = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61,
        67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179,
        181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293};

    private static class NodeLoop {

        public String start;
        public int loopLen;
        public int[] primeCts = new int[PRIMES.length];
        public int remainder;

        @Override
        public String toString() {
            return "(" + start + "," + loopLen + ":" + Arrays.toString(primeCts) + "," + remainder + ")";
        }

        public void findPrimes() {
            remainder = loopLen;
            for (int i = 0; i < PRIMES.length; i++) {
                while ((remainder % PRIMES[i]) == 0) {
                    remainder /= PRIMES[i];
                    primeCts[i]++;
                }
                if (remainder < PRIMES[i]) {
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        new Day08b().run(args, "b");
    }

    public Day08b() {
        super(false);
        setParser(new TwoFormatParser<>(
                new LineObjectParser<>(Day08Direction::byChar),
                new LineObjectMapParser<>(line -> new Day08Step(line), s -> s.id)
        ));
    }

    @Override
    public void solve(TwoFormatParser.Output<List<List<Day08Direction>>, Map<String, Day08Step>> data) {
        List<Day08Direction> directions = data.first.get(0);
        Map<String, Day08Step> steps = data.rest;
        LOG.info("Read {} directions, {} steps", directions.size(), steps.size());

        // Find the loop lengths and break each length into prime factors
        List<NodeLoop> loops = steps.keySet().stream()
                .filter(s -> s.endsWith("A"))
                .map(s -> findLoopLen(directions, steps, s))
                .peek(NodeLoop::findPrimes)
                .toList();
        LOG.info("Loops: {}", loops);

        // Find the set of primes in the LCM
        int[] factors = new int[PRIMES.length];
        for (NodeLoop loop : loops) {
            for (int i = 0; i < factors.length; i++) {
                factors[i] = Math.max(factors[i], loop.primeCts[i]);
            }
        }
        LOG.info("Prime factors: {}", Arrays.toString(factors));

        // Compute the LCM
        long answer = 1;
        for (int i = 0; i < factors.length; i++) {
            if (factors[i] > 0) {
                answer *= factors[i] * PRIMES[i];
            }
        }

        LOG.info("answer = {}", answer);
    }

    private NodeLoop findLoopLen(List<Day08Direction> directions, Map<String, Day08Step> steps, String start) {
        NodeLoop result = new NodeLoop();
        result.start = start;

        // Find first stop at an endpoint
        int count = 0;
        String currpos = start;
        while (!currpos.endsWith("Z")) {
            LOG.debug("step {}: at {}", count, steps.get(currpos));
            currpos = steps.get(currpos).next[directions.get(count % directions.size()).ordinal()];
            count++;
        }
        result.loopLen = count;

        // With current input data, the second loop is the same length as the first
        return result;
    }
}
