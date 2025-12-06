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

        int n1 = 0;
        int n2 = 0;
        int n3 = 0;
        for (int i = 0; ((i < entries.size()) && (n1 == 0)); i++) {
            for (int j = i + 1; ((j < entries.size()) && (n1 == 0)); j++) {
                int k = findMatch(entries, i, j);
                if (k > 0) {
                    n1 = entries.get(i);
                    n2 = entries.get(j);
                    n3 = entries.get(k);
                }
            }
        }

        LOG.info("n1={}, n2={}, n3={}, ans={}", n1, n2, n3, (n1 * n2 * n3));
    }

    private int findMatch(List<Integer> entries, int start1, int start2) {
        int n1 = entries.get(start1);
        int n2 = entries.get(start2);
        for (int i = start2 + 1; i < entries.size(); i++) {
            if ((n1 + n2 + entries.get(i)) == 2020) {
                return i;
            }
        }
        return 0;
    }
}
