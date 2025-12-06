package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.parser.ObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day19a extends BaseClass<TwoFormatParser.Output<Map<String, Day19Workflow>, List<Day19Part>>> {

    public static void main(String[] args) {
        new Day19a().run(args, "");
    }

    public Day19a() {
        super(true);
        setParser(new TwoFormatParser<>(
                new LineObjectMapParser<>(Day19Workflow::new, w -> w.name),
                new ObjectParser<>(Day19Part::new)
        ));
    }

    @Override
    public void solve(TwoFormatParser.Output<Map<String, Day19Workflow>, List<Day19Part>> data) {
        Map<String, Day19Workflow> workflows = data.first;
        List<Day19Part> parts = data.rest;
        LOG.info("Read {} workflows, {} parts", workflows.size(), parts.size());

        List<Day19Part> accepted = new ArrayList<>();
        List<Day19Part> rejected = new ArrayList<>();

        for (Day19Part part : parts) {
            String currflow = "in";
            while ((!currflow.equals("A")) && (!currflow.equals("R"))) {
                //LOG.debug("Running workflow {} on {}", currflow, part);
                currflow = workflows.get(currflow).process(part);
            }
            switch (currflow) {
                case "A" -> {accepted.add(part);}
                case "R" -> {rejected.add(part);}
                default -> {LOG.warn("How did I end with {}?", currflow);}
            }
        }
        LOG.debug("Finished with {} accepted and {} rejected", accepted.size(), rejected.size());
        
        long sum = 0;
        for (Day19Part part : accepted) {
            sum += part.a + part.m + part.s + part.x;
        }
        LOG.info("Total ratings is {}", sum);
    }
}
