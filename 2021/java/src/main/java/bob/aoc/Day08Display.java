package bob.aoc;

import bob.util.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class Day08Display {

    public enum Segment {
        TOP, UL, UR, MID, LL, LR, BOT;
    }

    public enum Wire {
        A, B, C, D, E, F, G;
    }

    private final List<EnumSet<Wire>> inputs;
    private final List<EnumSet<Wire>> outputs;
    private final EnumMap<Segment, Wire> mapping = new EnumMap<>(Segment.class);
    private final List<EnumSet<Wire>> digits = new ArrayList<>();

    public Day08Display(String line) {
        List<String> bits = Arrays.asList(line.split(" "));
        Assert.that((bits.size() == 15), "number of words in line not 15");

        inputs = bits.subList(0, 10).stream()
                .map(this::split)
                .collect(Collectors.toList());
        inputs.sort((a, b) -> (a.size() - b.size()));

        outputs = bits.subList(11, 15).stream()
                .map(this::split)
                .collect(Collectors.toList());
    }

    private EnumSet<Wire> split(String in) {
        String wires = in.toUpperCase();
        EnumSet<Wire> retval = EnumSet.noneOf(Wire.class);
        for (int i = 0; i < in.length(); i++) {
            retval.add(Wire.valueOf(wires.substring(i, (i + 1))));
        }
        return retval;
    }

    @Override
    public String toString() {
        return "in=" + inputs + ", out=" + outputs;
    }
}
