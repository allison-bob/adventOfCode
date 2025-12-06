package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day22b extends BaseClass<List<Day22Instruction>> {

    public static void main(String[] args) {
        new Day22b().run(args, "");
    }

    public Day22b() {
        super(false);
        setParser(new ObjectParser<>(Day22Instruction::new));
    }

    @Override
    public void solve(List<Day22Instruction> instructs) {
        LOG.info("Read {} instructions", instructs.size());

        // Follow the instructions
        Set<Day22Region> onRegions = new HashSet<>();
        for (Day22Instruction instr : instructs) {
            LOG.debug("Processing {}", instr);
            Day22Region newreg = new Day22Region(instr);

            // Find existing regions that overlap the new one
            List<Day22Region> overlapping = onRegions.stream()
                    .filter(r -> r.overlaps(newreg))
                    .collect(Collectors.toList());
            // Remove the regions to be processed from the main set
            onRegions.removeAll(overlapping);

            // Split up all the overlapping regions and the parts back into the main set
            onRegions.addAll(overlapping.stream()
                    .flatMap(r -> r.split(newreg))
                    .collect(Collectors.toSet())
            );

            // Add the new region
            if (instr.isTurnOn()) {
                onRegions.add(newreg);
            }
        }

        long cubesOn = onRegions.stream()
                .peek(r -> LOG.debug("Counting points in {}", r))
                .mapToLong(Day22Region::size)
                .peek(s -> LOG.debug("... size is {}", s))
                .sum();

        LOG.info("There are {} cubes turned on", cubesOn);
    }
}
