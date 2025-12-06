package bob.aoc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day07Hand implements Comparable<Day07Hand> {

    public enum Card {
        C2(1), C3(2), C4(3), C5(4), C6(5), C7(6), C8(7), C9(8), CT(9), CJ(0), CQ(10), CK(11), CA(12);

        public int part2rank;

        private Card(int part2rank) {
            this.part2rank = part2rank;
        }
    }

    public enum Types {
        HIGH, PAIR1, PAIR2, THREE, FULL, FOUR, FIVE;
    }

    public Card[] cards;
    public Types type;
    public long bid;
    public boolean isPart2;

    public Day07Hand(String line, boolean isPart2) {
        this.isPart2 = isPart2;
        cards = new Card[5];
        for (int i = 0; i < 5; i++) {
            cards[i] = Card.valueOf("C" + line.substring(i, (i + 1)));
        }
        bid = Long.parseLong(line.substring(6));

        Map<Card, List<Card>> cardStack = Stream.of(cards)
                .collect(Collectors.groupingBy(Function.identity()));
        int[] counts = cardStack.values().stream()
                .mapToInt(List::size)
                .sorted()
                .toArray();
        switch (counts.length) {
            case 1 -> {
                // All cards the same
                type = Types.FIVE;
            }
            case 2 -> {
                switch (counts[1]) {
                    case 4 -> {
                        // Four alike, one different
                        type = Types.FOUR;
                    }
                    case 3 -> {
                        // Three alike, two alike
                        type = Types.FULL;
                    }
                }
            }
            case 3 -> {
                switch (counts[2]) {
                    case 3 -> {
                        // Three alike, two different
                        type = Types.THREE;
                    }
                    case 2 -> {
                        // Two alike, two alike, one different
                        type = Types.PAIR2;
                    }
                }
            }
            case 4 -> {
                // Two alike, three different
                type = Types.PAIR1;
            }
            case 5 -> {
                // All different
                type = Types.HIGH;
            }
        }
        if (isPart2) {
            if (cardStack.containsKey(Card.CJ)) {
                System.out.println("adjusting " + this + " for " + cardStack.get(Card.CJ).size() + " Js");
                switch (cardStack.get(Card.CJ).size()) {
                    case 1 -> {
                        type = switch (type) {
                            case HIGH ->
                                Types.PAIR1;
                            case PAIR1 ->
                                Types.THREE;
                            case PAIR2 ->
                                Types.FULL;
                            case THREE ->
                                Types.FOUR;
                            case FULL ->
                                Types.FOUR;
                            case FOUR ->
                                Types.FIVE;
                            case FIVE ->
                                Types.FIVE;
                        };
                    }
                    case 2 -> {
                        type = switch (type) {
                            case PAIR1 ->
                                Types.THREE; // Pair of J
                            case PAIR2 ->
                                Types.FOUR;
                            case FULL ->
                                Types.FIVE; // Something over J
                            default ->
                                type;
                        };
                    }
                    case 3 -> {
                        type = switch (type) {
                            case THREE ->
                                Types.FOUR;
                            case FULL ->
                                Types.FIVE; // J over something
                            default ->
                                type;
                        };
                    }
                    case 4 -> {
                        type = switch (type) {
                            case FOUR ->
                                Types.FIVE;
                            default ->
                                type;
                        };
                    }
                }
                System.out.println("... resulting in " + this);
            }
        }
    }

    @Override
    public int compareTo(Day07Hand that) {
        int result = Integer.compare(this.type.ordinal(), that.type.ordinal());
        for (int i = 0; ((i < cards.length) && (result == 0)); i++) {
            if (isPart2) {
                result = Integer.compare(this.cards[i].part2rank, that.cards[i].part2rank);
            } else {
                result = Integer.compare(this.cards[i].ordinal(), that.cards[i].ordinal());
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "(" + type + ":" + Arrays.toString(cards) + ", " + bid + ")";
    }
}
