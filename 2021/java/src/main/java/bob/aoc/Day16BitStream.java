package bob.aoc;

public class Day16BitStream {

    private final String buffer;
    private int bytePos;
    private int bitPos;
    private long bitsLeft;

    public Day16BitStream(String line) {
        this.buffer = line;
    }

    public long getBits(int bitct) {
        if (done()) {
            return 0;
        }

        while (bitct > bitPos) {
            int newbits = Integer.parseInt(buffer.substring(bytePos, bytePos + 1), 16);
            bytePos++;
            bitPos += 4;
            bitsLeft = (bitsLeft << 4) + newbits;
        }

        int unused = bitPos - bitct;
        long mask = (1 << unused) - 1;

        long retval = bitsLeft >> unused;
        bitsLeft &= mask;
        bitPos = unused;

        return retval;
    }

    public void end() {
        bitsLeft = 0;
        bitPos = 0;
    }

    public boolean done() {
        return bytePos >= buffer.length();
    }
}
