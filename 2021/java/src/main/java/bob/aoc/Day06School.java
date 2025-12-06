package bob.aoc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

public class Day06School {

    private final long[] cts;

    public Day06School(List<Integer> states) {
        cts = new long[9];
        for (Integer s : states) {
            cts[s]++;
        }
    }

    public long size() {
        return LongStream.of(cts).sum();
    }

    public void nextDay() {
        long newct = cts[0];

        cts[0] = cts[1];
        cts[1] = cts[2];
        cts[2] = cts[3];
        cts[3] = cts[4];
        cts[4] = cts[5];
        cts[5] = cts[6];
        cts[6] = cts[7];
        cts[7] = cts[8];

        cts[8] = newct;
        cts[6] += newct;
    }

    @Override
    public String toString() {
        return Arrays.toString(cts);
    }
}
