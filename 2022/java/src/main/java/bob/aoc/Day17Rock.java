package bob.aoc;

import lombok.Getter;

@Getter
public enum Day17Rock {

    DASH(0x1E, 0x00, 0x00, 0x00),
    PLUS(0x08, 0x1C, 0x08, 0x00),
    J(0x1C, 0x04, 0x04, 0x00),
    PIPE(0x10, 0x10, 0x10, 0x10),
    DOT(0x18, 0x18, 0x00, 0x00);

    private final int[] shape;
    private Day17Rock next;
    static {
        DASH.next = PLUS;
        PLUS.next = J;
        J.next = PIPE;
        PIPE.next = DOT;
        DOT.next = DASH;
    }

    private Day17Rock(int... shape) {
        this.shape = shape;
    }

    @Override
    public String toString() {
        return asString(shape);
    }

    public static String asString(int[] in) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < in.length; i++) {
            sb.append(",").append(Integer.toHexString(in[i]));
        }
        sb.replace(0, 1, "[");
        sb.append("]");
        return sb.toString();
    }
}
