package bob.aoc;

public enum Day17State {
    INACTIVE('.'), ACTIVE('#');

    public char symbol;

    private Day17State(char symbol) {
        this.symbol = symbol;
    }

    public static Day17State byChar(char in) {
        for (Day17State at : Day17State.values()) {
            if (at.symbol == in) {
                return at;
            }
        }
        throw new IllegalArgumentException("Unknown State symbol: " + in);
    }
}
