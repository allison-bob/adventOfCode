package bob.aoc;

public record Day20Entry(int origPos, long value) {

    @Override
    public String toString() {
        return "(" + origPos + "," + value + ")";
    }

}
