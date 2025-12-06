package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.Stack;

public class Day10a extends BaseClass<List<String>> {

    public static void main(String[] args) {
        new Day10a().run(args, "");
    }

    public Day10a() {
        super(false);
        setParser(new ObjectParser<>(line -> line));
    }

    @Override
    public void solve(List<String> lines) {
        LOG.info("read {} lines", lines.size());
        
        long score = 0;
        for (String line : lines) {
            long lscore = parse(line);
            score += lscore;
            LOG.debug("line {} scores {}, total is now {}", line, lscore, score);
        }
        
        LOG.info("Answer is {}", score);
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
                    Day10Delim wrong = Day10Delim.findByClose(chars[i]);
                    return wrong.getErrorScore();
                }
            }
        }
        
        return 0;
    }
}
