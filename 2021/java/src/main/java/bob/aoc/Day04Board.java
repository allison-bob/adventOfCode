package bob.aoc;

public class Day04Board {

    private final int[][] numbers = new int[5][];
    private final boolean[][] called = new boolean[5][];
    private int rowct;

    public void addRow(String line) {
        numbers[rowct] = new int[5];
        called[rowct] = new boolean[5];
        for (int j = 0; j < 5; j++) {
            int pos = (3 * j);
            numbers[rowct][j] = Integer.parseInt(line.substring(pos, pos + 2).trim());
        }
        rowct++;
    }

    public void mark(int toMark) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (numbers[i][j] == toMark) {
                    called[i][j] = true;
                }
            }
        }
    }

    public boolean isWinner() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boolean test = called[i][0];
                for (int k = 1; k < 5; k++) {
                    test &= called[i][k];
                }
                if (test) {
                    return true;
                }

                test = called[0][j];
                for (int k = 1; k < 5; k++) {
                    test &= called[k][j];
                }
                if (test) {
                    return true;
                }
            }
        }

        return false;
    }

    public int sumUncalled() {
        int sum = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (!called[i][j]) {
                    sum += numbers[i][j];
                }
            }
        }
        return sum;
    }
}
