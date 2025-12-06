package bob.aoc;

import bob.util.BaseClass;
import bob.parser.SingleIntegerParser;
import java.util.List;

public class Day01b extends BaseClass<List<Integer>> {

    public static void main(String[] args) {
        new Day01b().run(args, "");
    }

    public Day01b() {
        super(false);
        setParser(new SingleIntegerParser());
    }

    @Override
    public void solve(List<Integer> entries) {
        LOG.info("Read {} entries", entries.size());

        int ct = 0;
        
        for (int i = 3; i < entries.size(); i++) {
            int s0 = entries.get(i - 3) + entries.get(i - 2) + entries.get(i - 1);
            int s1 = entries.get(i - 2) + entries.get(i - 1) + entries.get(i - 0);
            if (s1 > s0) {
                ct++;
            }
        }

        LOG.info("There are {} increases", ct);
    }
}
