package bob.aoc;

import lombok.Getter;

@Getter
public enum Day02Play {
    ROCK('A', 'X', 1),
    PAPER('B', 'Y', 2),
    SCISSORS('C', 'Z', 3);

    private final char p1in;
    private final char p2in;
    private final int score;
    private Day02Play defeats;
    private Day02Play losesTo;

    private Day02Play(char p1in, char p2in, int score) {
        this.p1in = p1in;
        this.p2in = p2in;
        this.score = score;
    }
    
    static {
        ROCK.defeats = SCISSORS;
        ROCK.losesTo = PAPER;
        PAPER.defeats = ROCK;
        PAPER.losesTo = SCISSORS;
        SCISSORS.defeats = PAPER;
        SCISSORS.losesTo = ROCK;
    }

    public static Day02Play byChar(char toTest) {
        for (Day02Play v : values()) {
            if (v.p1in == toTest) {
                return v;
            }
            if (v.p2in == toTest) {
                return v;
            }
        }
        return null;
    }
}
