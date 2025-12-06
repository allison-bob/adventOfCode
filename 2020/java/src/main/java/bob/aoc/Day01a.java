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

        int n1 = 0;
        int n2 = 0;
        for (int i = 0; ((i < entries.size()) && (n1 == 0)); i++) {
            int j = findMatch(entries, i);
            if (j > 0) {
                n1 = entries.get(i);
                n2 = entries.get(j);
            }
        }

        LOG.info("n1={}, n2={}, ans={}", n1, n2, (n1 * n2));
    }

    private int findMatch(List<Integer> entries, int start) {
        int n1 = entries.get(start);
        for (int i = start + 1; i < entries.size(); i++) {
            if ((n1 + entries.get(i)) == 2020) {
                return i;
            }
        }
        return 0;
    }
}
