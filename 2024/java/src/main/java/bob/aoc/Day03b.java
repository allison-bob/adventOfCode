package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03b extends BaseClass<List<String>> {

    public static void main(String[] args) {
        new Day03b().run(args, "");
    }

    public Day03b() {
        super(false);
        setParser(new ObjectParser<>(Function.identity()));
    }

    @Override
    public void solve(List<String> entries) {
        LOG.info("Read {} entries", entries.size());

        Pattern patt = Pattern.compile("mul[(]([0-9]+),([0-9]+)[)]|do[(][)]|don't[(][)]");

        long total = 0;
        boolean enabled = true;
        for (String e : entries) {
            LOG.debug("Processing {}", e);
            Matcher m = patt.matcher(e);
            while (m.find()) {
                LOG.debug("found from {} to {}: {}: {}, {}", m.start(), m.end(), m.group(), m.group(1), m.group(2));
                switch (m.group()) {
                    case "do()" -> enabled = true;
                    case "don't()" -> enabled = false;
                    default -> {
                        if (enabled) {
                            total += Long.parseLong(m.group(1)) * Long.parseLong(m.group(2));
                        }
                    }
                }
                LOG.debug("Enabled is now {}, Total is now {}", enabled, total);
            }
        }

        LOG.info("total: {}", total);
    }
}
