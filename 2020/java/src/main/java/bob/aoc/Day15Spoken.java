package bob.aoc;

import java.util.ArrayList;
import java.util.List;

public class Day15Spoken {

        private final List<Integer> saidOn = new ArrayList<>();

        public Day15Spoken(int turn) {
            saidOn.add(turn);
        }

        public void wasSaid(int turn) {
            saidOn.add(0, turn);
            if (saidOn.size() > 2) {
                saidOn.remove(2);
            }
        }

        public boolean notSaidBefore() {
            return saidOn.size() < 2;
        }

        public int lastSaidAge() {
            return saidOn.get(0) - saidOn.get(1);
        }

        @Override
        public String toString() {
            return saidOn.toString();
        }
}
