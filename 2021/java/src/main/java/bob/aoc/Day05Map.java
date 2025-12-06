package bob.aoc;

import org.slf4j.Logger;

public class Day05Map {

    private final int[][] points;
    private final Logger LOG;

    public Day05Map(int size, Logger LOG) {
        points = new int[size + 1][size + 1];
        this.LOG = LOG;
    }

    public void plot(Day05Line line, boolean doDiag) {
        if (line.isHorizontal()) {
            LOG.debug("plotting horiz line " + line);
            int y = line.y1;
            for (int x = Math.min(line.x1, line.x2); x <= Math.max(line.x1, line.x2); x++) {
                points[y][x]++;
            }
        } else if (line.isVertical()) {
            LOG.debug("plotting vert line " + line);
            int x = line.x1;
            for (int y = Math.min(line.y1, line.y2); y <= Math.max(line.y1, line.y2); y++) {
                points[y][x]++;
            }
        } else {
            LOG.debug("plotting other line " + line);
            if (doDiag) {
                int xincr = (int) Math.signum(line.x2 - line.x1);
                int yincr = (int) Math.signum(line.y2 - line.y1);
                int x = line.x1;
                int y = line.y1;
                while ((x != line.x2) || (y != line.y2)) {
                    points[y][x]++;
                    x += xincr;
                    y += yincr;
                }
                points[y][x]++;
            }
        }
    }

    public int countAbove(int minval) {
        int result = 0;

        for (int i = 0; i < points.length; i++) {
            int[] row = points[i];
            for (int j = 0; j < row.length; j++) {
                if (points[i][j] > minval) {
                    result++;
                }
            }
        }

        return result;
    }
}
