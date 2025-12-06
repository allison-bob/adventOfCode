package bob.aoc;

import bob.parser.ObjectListParser;
import bob.parser.LineObjectMapParser;
import bob.util.BaseClass;
import bob.parser.TwoFormatParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day16a extends BaseClass<TwoFormatParser.Output<Map<String, Day16ValidRanges>, List<List<Day16Ticket>>>> {

    public static void main(String[] args) {
        new Day16a().run(args, "");
    }

    public Day16a() {
        super(false);
        setParser(new TwoFormatParser<>(
                new LineObjectMapParser<>(Day16ValidRanges::new, Day16ValidRanges::getFieldname),
                new ObjectListParser<>(ArrayList::new, (list, line) -> list.add(new Day16Ticket(line)))
        ));
    }

    @Override
    public void solve(TwoFormatParser.Output<Map<String, Day16ValidRanges>, List<List<Day16Ticket>>> data) {
        Map<String, Day16ValidRanges> fields = data.first;
        Day16Ticket myTicket = data.rest.get(0).get(1);
        List<Day16Ticket> tickets = data.rest.get(1);
        tickets.remove(0);
        LOG.info("read {} rules and {} nearby tickets", fields.size(), tickets.size());

        // Find the sum of all values that aren't valid for any field
        int badsum = 0;
        for (Day16Ticket t : tickets) {
            int[] values = t.getValues();
            for (int i = 0; i < values.length; i++) {
                boolean valid = false;
                for (Day16ValidRanges v : fields.values()) {
                    valid |= v.isValid(values[i]);
                }
                if (!valid) {
                    badsum += values[i];
                }
                LOG.debug("value {} valid? {}, sum is {}", values[i], valid, badsum);
            }
        }

        LOG.info("bad sum is {}", badsum);
    }
}
