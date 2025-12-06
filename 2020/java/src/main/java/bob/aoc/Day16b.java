package bob.aoc;

import bob.parser.ObjectListParser;
import bob.parser.LineObjectMapParser;
import bob.util.Assert;
import bob.util.BaseClass;
import bob.parser.TwoFormatParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Day16b extends BaseClass<TwoFormatParser.Output<Map<String, Day16ValidRanges>, List<List<Day16Ticket>>>> {

    public static void main(String[] args) {
        new Day16b().run(args, "");
    }

    public Day16b() {
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

        // Invalidate tickets with a value that isn't valid for any field
        for (Day16Ticket t : tickets) {
            int[] values = t.getValues();
            for (int i = 0; i < values.length; i++) {
                boolean valid = false;
                for (Day16ValidRanges v : fields.values()) {
                    valid |= v.isValid(values[i]);
                }
                if (!valid) {
                    t.setValid(false);
                }
                LOG.debug("value {} valid? {}", values[i], valid);
            }
        }

        // Assume my ticket has all valid values, use it to seed the list of possible field numbers
        for (Day16ValidRanges vr : fields.values()) {
            vr.addPossibles(myTicket.getValues());
        }

        // Go through the nearby valid tickets to remove anything that doesn't work
        for (Day16Ticket t : tickets) {
            if (t.isValid()) {
                for (Day16ValidRanges vr : fields.values()) {
                    vr.removePossibles(t.getValues());
                }
            }
        }

        // Build the field sequence
        Map<Integer, String> order = new TreeMap<>();
        boolean repeat = true;
        while (repeat) {
            repeat = false;
            for (Map.Entry<String, Day16ValidRanges> e : fields.entrySet()) {
                if (e.getValue().getPossibleFieldNums().isEmpty()) {
                    // This field is already placed
                } else if (e.getValue().getPossibleFieldNums().size() == 1) {
                    // This field can only be in one place
                    order.put(e.getValue().getPossibleFieldNums().first(), e.getKey());
                } else {
                    // This field still has multiple options
                    repeat = true;
                }
            }

            // Remove the current placed fields from all possibles
            for (Day16ValidRanges vr : fields.values()) {
                vr.getPossibleFieldNums().removeAll(order.keySet());
            }
        }
        LOG.debug("order={}", order);
        Assert.that((fields.size() == order.size()), "Placed field count");

        // Compute the answer
        long product = order.entrySet().stream()
                .filter(oe -> oe.getValue().startsWith("departure"))
                .mapToInt(oe -> oe.getKey())
                .mapToLong(idx -> myTicket.getValues()[idx])
                .reduce(1, Math::multiplyExact);

        LOG.info("answer = {}", product);
    }
}
