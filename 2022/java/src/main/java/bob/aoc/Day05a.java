package bob.aoc;

import bob.parser.ObjectListParser;
import bob.parser.ObjectParser;
import bob.parser.TwoFormatParser;
import bob.util.BaseClass;
import java.util.List;

public class Day05a extends BaseClass<TwoFormatParser.Output<List<Day05Yard>, List<Day05Instruction>>> {

    public static void main(String[] args) {
        new Day05a().run(args, "");
    }

    public Day05a() {
        super(false);
        setParser(new TwoFormatParser<>(
                new ObjectListParser<>(Day05Yard::new, Day05Yard::add),
                new ObjectParser<>(Day05Instruction::new)
        ));
    }

    @Override
    public void solve(TwoFormatParser.Output<List<Day05Yard>, List<Day05Instruction>> data) {
        Day05Yard yard = data.first.get(0);
        List<Day05Instruction> instrs = data.rest;
        LOG.info("Read {} instructions", instrs.size());
        LOG.debug("Initial stacks:{}", yard);
        if (LOG.isDebugEnabled()) {
            instrs.forEach(i -> LOG.debug(i.toString()));
        }

        // Perform the moves
        for (Day05Instruction instr : instrs) {
            LOG.debug("Performing: {}", instr);
            for (int i = 0; i < instr.getCount(); i++) {
                yard.move(instr.getFrom(), instr.getTo());
            }
            LOG.debug("New State:{}", yard);
        }
        
        LOG.info("Answer is {}", yard.tops());
    }
}
