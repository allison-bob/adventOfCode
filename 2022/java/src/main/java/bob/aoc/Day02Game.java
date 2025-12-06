package bob.aoc;

import bob.util.Assert;

public class Day02Game {

    final Day02Play you;
    final Day02Play me;

    public Day02Game(String line, boolean isPart2) {
        you = Day02Play.byChar(line.charAt(0));
        if (isPart2) {
            me = switch (line.charAt(2)) {
                case 'X' ->
                    you.getDefeats();
                case 'Y' ->
                    you;
                case 'Z' ->
                    you.getLosesTo();
                default ->
                    throw Assert.failed(null, "Unexpected intended outcome");
            };
        } else {
            me = Day02Play.byChar(line.charAt(2));
        }
    }
}
