package bob.aoc;

import bob.parser.LineListParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day02b extends BaseClass<List<List<Long>>> {

    public static void main(String[] args) {
        new Day02b().run(args, "");
    }

    public Day02b() {
        super(false);
        setParser(new LineListParser<>(" ", Long::valueOf));
    }

    @Override
    public void solve(List<List<Long>> entries) {
        LOG.info("Read {} entries", entries.size());
        LOG.debug(entries.toString());

        int safeCt = 0;
        for (List<Long> e : entries) {
            if (isSafe(e)) {
                safeCt++;
            } else {
                boolean skipSafe = false;
                for (int i = 0; ((i < e.size()) && (!skipSafe)); i++) {
                    List<Long> e1 = new ArrayList<>(e);
                    e1.remove(i);
                    if (isSafe(e1)) {
                        skipSafe = true;
                    }
                }
                if (skipSafe) {
                    safeCt++;
                }
            }
        }

        LOG.info("safe count: {}", safeCt);
    }

    private boolean isSafe(List<Long> report) {
        LOG.debug("Checking {}", report);
        long diff = report.get(0) - report.get(1);
        if (diff < 0) {
            for (int i = 1; i < report.size(); i++) {
                if ((report.get(i - 1) - report.get(i)) >= 0) {
                    LOG.debug("Reverses direction at {}", i);
                    return false;
                }
                if ((report.get(i - 1) - report.get(i)) < -3) {
                    LOG.debug("Excessive change at {}", i);
                    return false;
                }
            }
            return true;
        } else if (diff > 0) {
            for (int i = 1; i < report.size(); i++) {
                if ((report.get(i - 1) - report.get(i)) <= 0) {
                    LOG.debug("Reverses direction at {}", i);
                    return false;
                }
                if ((report.get(i - 1) - report.get(i)) > 3) {
                    LOG.debug("Excessive change at {}", i);
                    return false;
                }
            }
            return true;
        }

        // First two entries match
        return false;
    }
}
