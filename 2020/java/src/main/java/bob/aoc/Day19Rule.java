package bob.aoc;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day19Rule {

    @Getter
    private final int ruleNum;
    @Getter
    private final List<Object> elements = new ArrayList<>();
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public Day19Rule(String line) {
        int pos = line.indexOf(": ");
        ruleNum = Integer.parseInt(line.substring(0, pos));
        String[] bits = line.substring(pos + 2).split(" ");
        for (String b : bits) {
            if (b.equals("|")) {
                elements.add('|');
            } else if (b.startsWith("\"")) {
                elements.add(b.substring(1, b.length() - 1));
            } else {
                elements.add(Integer.valueOf(b));
            }
        }
        LOG.debug("read: {} == {}", ruleNum, elements);
    }
}
