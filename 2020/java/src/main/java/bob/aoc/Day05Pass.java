package bob.aoc;

import bob.util.Assert;

public class Day05Pass {

    public int row;
    public int seat;
    public int id;

    public Day05Pass(String code) {
        Assert.that((code.length() == 10), "Code length");
        String bits = code.replace('F', '0').replace('B', '1').replace('L', '0').replace('R', '1');
        Assert.that(bits.matches("^[01]{10}$"), "Invalid characters in boarding pass");

        row = Integer.parseInt(bits.substring(0, 7), 2);
        seat = Integer.parseInt(bits.substring(7, 10), 2);
        id = Integer.parseInt(bits, 2);
    }
}
