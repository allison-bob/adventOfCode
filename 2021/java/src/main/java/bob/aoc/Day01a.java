package bob.aoc;

import bob.util.BaseClass;
import bob.parser.SingleIntegerParser;
import java.util.List;

public class Day01a extends BaseClass<List<Integer>> {

    public static void main(String[] args) {
        new Day01a().run(args, "");
    }

    public Day01a() {
        super(false);
        setParser(new SingleIntegerParser());
    }

    @Override
    public void solve(List<Integer> entries) {
        LOG.info("Read {} entries", entries.size());

        int ct = 0;
        
        for (int i = 1; i < entries.size(); i++) {
            if (entries.get(i) > entries.get(i - 1)) {
                ct++;
            }
        }

        LOG.info("There are {} increases", ct);
    }
}
