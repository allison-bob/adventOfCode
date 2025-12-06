package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

public class Day08b extends BaseClass<List<Day08Display>> {

    public static void main(String[] args) {
        new Day08b().run(args, "");
    }

    public Day08b() {
        super(false);
        setParser(new ObjectParser<>(Day08Display::new));
    }

    @Override
    public void solve(List<Day08Display> displays) {
        LOG.info("read {} displays", displays.size());

        long answer = displays.stream()
                .peek(this::findMapping)
                .peek(this::findDigits)
                .mapToLong(this::decode)
                .sum();

        LOG.info("Answer is {}", answer);
    }

    private long decode(Day08Display disp) {
        long result = 0;
        List<EnumSet<Day08Display.Wire>> digits = disp.getDigits();
        
        for (EnumSet<Day08Display.Wire> o : disp.getOutputs()) {
            result = (10 * result) + digits.indexOf(o);
        }
        LOG.debug("decoded value is {}", result);

        return result;
    }

    private void findDigits(Day08Display disp) {
        EnumMap<Day08Display.Segment, Day08Display.Wire> mapping = disp.getMapping();
        List<EnumSet<Day08Display.Wire>> digits = disp.getDigits();

        // 0
        digits.add(EnumSet.of(
                mapping.get(Day08Display.Segment.TOP), mapping.get(Day08Display.Segment.UL),
                mapping.get(Day08Display.Segment.UR), mapping.get(Day08Display.Segment.LL),
                mapping.get(Day08Display.Segment.LR), mapping.get(Day08Display.Segment.BOT)
        ));

        // 1
        digits.add(EnumSet.of(
                mapping.get(Day08Display.Segment.UR), mapping.get(Day08Display.Segment.LR)
        ));

        // 2
        digits.add(EnumSet.of(
                mapping.get(Day08Display.Segment.TOP), mapping.get(Day08Display.Segment.UR),
                mapping.get(Day08Display.Segment.MID), mapping.get(Day08Display.Segment.LL),
                mapping.get(Day08Display.Segment.BOT)
        ));

        // 3
        digits.add(EnumSet.of(
                mapping.get(Day08Display.Segment.TOP), mapping.get(Day08Display.Segment.UR),
                mapping.get(Day08Display.Segment.MID), mapping.get(Day08Display.Segment.LR),
                mapping.get(Day08Display.Segment.BOT)
        ));

        // 4
        digits.add(EnumSet.of(
                mapping.get(Day08Display.Segment.UL), mapping.get(Day08Display.Segment.UR),
                mapping.get(Day08Display.Segment.MID), mapping.get(Day08Display.Segment.LR)
        ));

        // 5
        digits.add(EnumSet.of(
                mapping.get(Day08Display.Segment.TOP), mapping.get(Day08Display.Segment.UL),
                mapping.get(Day08Display.Segment.MID), mapping.get(Day08Display.Segment.LR),
                mapping.get(Day08Display.Segment.BOT)
        ));

        // 6
        digits.add(EnumSet.of(
                mapping.get(Day08Display.Segment.TOP), mapping.get(Day08Display.Segment.UL),
                mapping.get(Day08Display.Segment.MID), mapping.get(Day08Display.Segment.LR),
                mapping.get(Day08Display.Segment.LL), mapping.get(Day08Display.Segment.BOT)
        ));

        // 7
        digits.add(EnumSet.of(
                mapping.get(Day08Display.Segment.TOP), mapping.get(Day08Display.Segment.UR),
                mapping.get(Day08Display.Segment.LR)
        ));

        // 8
        digits.add(EnumSet.of(
                mapping.get(Day08Display.Segment.TOP), mapping.get(Day08Display.Segment.UL),
                mapping.get(Day08Display.Segment.UR), mapping.get(Day08Display.Segment.MID),
                mapping.get(Day08Display.Segment.LL), mapping.get(Day08Display.Segment.LR),
                mapping.get(Day08Display.Segment.BOT)
        ));

        // 9
        digits.add(EnumSet.of(
                mapping.get(Day08Display.Segment.TOP), mapping.get(Day08Display.Segment.UL),
                mapping.get(Day08Display.Segment.UR), mapping.get(Day08Display.Segment.MID),
                mapping.get(Day08Display.Segment.LR), mapping.get(Day08Display.Segment.BOT)
        ));
    }

    private void findMapping(Day08Display disp) {
        List<EnumSet<Day08Display.Wire>> inputs = disp.getInputs();
        EnumMap<Day08Display.Segment, Day08Display.Wire> mapping = disp.getMapping();

        // Grab the wire sets for known numbers
        EnumSet<Day08Display.Wire> one = inputs.get(0);
        EnumSet<Day08Display.Wire> seven = inputs.get(1);
        EnumSet<Day08Display.Wire> four = inputs.get(2);

        // Top segment is the one in 7 that is missing in 1
        mapping.put(Day08Display.Segment.TOP, missing(seven, one));
        LOG.debug("TOP is {}", mapping.get(Day08Display.Segment.TOP));

        // Find the off segments in the six-light patterns
        EnumSet<Day08Display.Wire> oneOffSet = EnumSet.of(
                missing(inputs.get(9), inputs.get(6)),
                missing(inputs.get(9), inputs.get(7)),
                missing(inputs.get(9), inputs.get(8))
        );
        LOG.debug("oneOffSet is {}" + oneOffSet);

        // Upper-right segment is the member of oneOffSet also in 1
        // Lower-right segment is the other segment in 1
        for (Day08Display.Wire w : one) {
            if (oneOffSet.contains(w)) {
                mapping.put(Day08Display.Segment.UR, w);
                LOG.debug("UR is {}", mapping.get(Day08Display.Segment.UR));
            } else {
                mapping.put(Day08Display.Segment.LR, w);
                LOG.debug("LR is {}", mapping.get(Day08Display.Segment.LR));
            }
        }

        // Middle segment is the member of oneOffSet also in 4
        // Upper-left segment is the segment in 4 not in oneOffSet or 1
        for (Day08Display.Wire w : four) {
            if (one.contains(w)) {
                // Also in one, so already found
            } else if (oneOffSet.contains(w)) {
                mapping.put(Day08Display.Segment.MID, w);
                LOG.debug("MID is {}", mapping.get(Day08Display.Segment.MID));
            } else {
                mapping.put(Day08Display.Segment.UL, w);
                LOG.debug("UL is {}", mapping.get(Day08Display.Segment.UL));
            }
        }

        // Lower-left segment is the member of oneOffSet not yet found
        for (Day08Display.Wire w : oneOffSet) {
            if (!mapping.containsValue(w)) {
                mapping.put(Day08Display.Segment.LL, w);
                LOG.debug("LL is {}", mapping.get(Day08Display.Segment.LL));
            }
        }

        // Bottom segment is the remaining unfound
        EnumSet<Day08Display.Wire> s = EnumSet.allOf(Day08Display.Wire.class);
        s.removeAll(mapping.values());
        mapping.put(Day08Display.Segment.BOT, s.iterator().next());
        LOG.debug("BOT is {}", mapping.get(Day08Display.Segment.BOT));
    }

    private Day08Display.Wire missing(EnumSet<Day08Display.Wire> larger, EnumSet<Day08Display.Wire> smaller) {
        Assert.that(((larger.size() - smaller.size()) == 1), "larger set not one longer than smaller set");
        EnumSet<Day08Display.Wire> s = EnumSet.noneOf(Day08Display.Wire.class);
        s.addAll(larger);
        s.removeAll(smaller);
        return s.iterator().next();
    }
}
