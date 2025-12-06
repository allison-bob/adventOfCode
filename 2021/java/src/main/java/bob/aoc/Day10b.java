package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Day10b extends BaseClass<List<String>> {

    public static void main(String[] args) {
        new Day10b().run(args, "");
    }

    public Day10b() {
        super(false);
        setParser(new ObjectParser<>(line -> line));
    }

    @Override
    public void solve(List<String> lines) {
        LOG.info("read {} lines", lines.size());
        
        List<Long> scores = new ArrayList<>();
        for (String line : lines) {
            long lscore = parse(line);
            LOG.debug("line {} scores {}", line, lscore);
            if (lscore > 0) {
                scores.add(lscore);
            }
        }
        
        // Find the median value
        scores.sort(null);
        LOG.debug("scores are {}", scores);
        int mid = scores.size() / 2;
        
        LOG.info("Answer is {}", scores.get(mid));
    }
    
    private long parse(String line) {
        char[] chars = line.toCharArray();
        Stack<Day10Delim> stack = new Stack<>();
        
        for (int i = 0; i < chars.length; i++) {
            Day10Delim o = Day10Delim.findByOpen(chars[i]);
            if (o != null) {
                stack.push(o);
            } else {
                Day10Delim done = stack.pop();
                if (done.getClose() != chars[i]) {
                    return -1;
                }
            }
        }
        
        long result = 0;
        while (!stack.isEmpty()) {
            Day10Delim next = stack.remove(stack.size() - 1);
            result = (5 * result) + next.getComplScore();
        }

        return result;
    }
}
