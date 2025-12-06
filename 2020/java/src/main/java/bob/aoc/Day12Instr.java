package bob.aoc;

import bob.data.CPU;
import bob.data.coordinate.Coord2D;
import bob.data.coordinate.Mapper2D;

/**
 * CPU instructions.
 * <p>
 * The implementation uses the following CPU registers:</p>
 * <ul>
 * <li>Register 0 is the ship's current X coordinate</li>
 * <li>Register 1 is the ship's current Y coordinate</li>
 * <li>Register 2 is the ship's waypoint X coordinate</li>
 * <li>Register 3 is the ship's waypoint Y coordinate</li>
 * <li>Register 4 is 0 for part 1 and 2 for part 2, identifying which registers get modified for N, S, E, and W</li>
 * </ul>
 */
public class Day12Instr implements CPU.Operation {

    public enum OpCode {
        N, S, E, W, L, R, F;
    };

    private static final Mapper2D[] LEFT = new Mapper2D[]{
        Mapper2D.EN, Mapper2D.NW, Mapper2D.WS, Mapper2D.SE
    };
    private static final Mapper2D[] RIGHT = new Mapper2D[]{
        Mapper2D.EN, Mapper2D.SE, Mapper2D.WS, Mapper2D.NW
    };

    public OpCode op;
    public int value;

    public Day12Instr(String line) {
        op = OpCode.valueOf(line.substring(0, 1));
        value = Integer.parseInt(line.substring(1));
    }

    public void execute(CPU cpu) {
        long[] reg = cpu.getReg();
        int offset = (int) reg[4];
        switch (op) {
            case N ->
                reg[offset + 1] += value;
            case S ->
                reg[offset + 1] -= value;
            case E ->
                reg[offset + 0] += value;
            case W ->
                reg[offset + 0] -= value;
            case L ->
                rotate(reg, LEFT[value / 90]);
            case R ->
                rotate(reg, RIGHT[value / 90]);
            case F -> {
                reg[0] += value * reg[2];
                reg[1] += value * reg[3];
            }
        }
        cpu.setPc(cpu.getPc() + 1);
    }

    private void rotate(long[] reg, Mapper2D mapper) {
        Coord2D d = new Coord2D((int) reg[2], (int) reg[3]);
        d = mapper.map(d);
        reg[2] = d.getX();
        reg[3] = d.getY();
    }

    @Override
    public String toString() {
        return op + ": " + value;
    }
}
