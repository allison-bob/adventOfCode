package bob.aoc;

import lombok.Getter;

@Getter
public class Day22Instruction {
    
    private final int turn;
    private final int move;

    public Day22Instruction(String turn, int move) {
        this.turn = switch (turn) {
            case "L" -> 3;
            case "R" -> 1;
            default -> 0;
        };
        this.move = move;
    }

    @Override
    public String toString() {
        return "[" + move + "," + turn + "]";
    }
}
