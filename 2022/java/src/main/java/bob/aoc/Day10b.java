package bob.aoc;

import bob.data.CPU;
import bob.data.grid.Grid2D;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day10b extends BaseClass<List<Day10Instr>> {

    private final Grid2D<Day10Pixel> display = new Grid2D<>();
    private List<Day10Pixel> row = new ArrayList<>();

    public static void main(String[] args) {
        new Day10b().run(args, "");
    }

    public Day10b() {
        super(false);
        setParser(new ObjectParser<>(Day10Instr::new));
    }

    @Override
    public void solve(List<Day10Instr> program) {
        LOG.info("program contains {} instructions", program.size());

        CPU<Day10Instr> cpu = new CPU<>(program, this::prestep, this::poststep);
        cpu.getReg()[0] = 1;
        cpu.run();

        LOG.info("Answer is\n{}", display.dump(Day10Pixel::getSymbol));
    }

    private boolean prestep(CPU<Day10Instr> cpu) {
        LOG.debug("Executing {}, reg={},{},{}", cpu.getProgram().get(cpu.getPc()),
                cpu.getReg()[0], cpu.getReg()[1], cpu.getReg()[2]);
        if (cpu.getProgram().get(cpu.getPc()).getOp() == Day10Instr.OpCode.ADDX) {
            addToRow(cpu.getReg()[0]);
        }
        return true;
    }

    public boolean poststep(CPU<Day10Instr> cpu) {
        addToRow(cpu.getReg()[2]);
        return true;
    }

    private void addToRow(long sprite) {
        Day10Pixel toAdd;
        if (Math.abs(row.size() - sprite) < 2) {
            toAdd = Day10Pixel.ON;
        } else {
            toAdd = Day10Pixel.OFF;
        }
        LOG.debug("pos={}, sprite={}, toAdd={}", row.size(), sprite, toAdd);
        row.add(toAdd);
        if (row.size() >= 40) {
            display.addRow(row);
            row.clear();
        }
    }
}
