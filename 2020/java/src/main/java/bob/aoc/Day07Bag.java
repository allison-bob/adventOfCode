package bob.aoc;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Day07Bag {

        private String color;
        private int quantity;
        private List<Day07Bag> contents = new ArrayList<>();

        public Day07Bag() {
        }

        public Day07Bag(String line) {
            String[] bits = findNext(line, " bags contain ");
            color = bits[0];
            if (bits[1].equals("no other bags.")) {
                return;
            }
            while (!bits[1].isEmpty()) {
                Day07Bag within = new Day07Bag();
                bits = findNext(bits[1], " ");
                within.quantity = Integer.parseInt(bits[0]);
                quantity += within.quantity;
                bits = findNext(bits[1], " bag");
                within.color = bits[0];
                contents.add(within);
                if (bits[1].startsWith("s")) {
                    bits[1] = bits[1].substring(1);
                }
                if (bits[1].startsWith(",")) {
                    bits[1] = bits[1].substring(1);
                }
                if (bits[1].startsWith(".")) {
                    bits[1] = bits[1].substring(1);
                }
                if (bits[1].startsWith(" ")) {
                    bits[1] = bits[1].substring(1);
                }
            }
        }

        private String[] findNext(String line, String toFind) {
            int pos = line.indexOf(toFind);
            if (pos == -1) {
                return new String[] {line, ""};
            }
            return new String[] {line.substring(0, pos), line.substring(pos + toFind.length())};
        }

        @Override
        public String toString() {
            return color + "(" + quantity + "): " + contents;
        }
}
