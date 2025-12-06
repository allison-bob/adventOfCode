package bob.aoc;

public enum Day10Delim {
    PAREN('(', ')', 3, 1),
    BRACK('[', ']', 57, 2),
    BRACE('{', '}', 1197, 3),
    ANGLE('<', '>', 25137, 4);

    private final char open;
    private final char close;
    private final long errorScore;
    private final long complScore;

    private Day10Delim(char open, char close, long errorScore, long complScore) {
        this.open = open;
        this.close = close;
        this.errorScore = errorScore;
        this.complScore = complScore;
    }

    public char getOpen() {
        return open;
    }

    public char getClose() {
        return close;
    }

    public long getErrorScore() {
        return errorScore;
    }

    public long getComplScore() {
        return complScore;
    }

    public static Day10Delim findByOpen(char toTest) {
        for (Day10Delim v : values()) {
            if (v.getOpen() == toTest) {
                return v;
            }
        }
        return null;
    }

    public static Day10Delim findByClose(char toTest) {
        for (Day10Delim v : values()) {
            if (v.getClose() == toTest) {
                return v;
            }
        }
        return null;
    }
}
