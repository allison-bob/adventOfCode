package bob.aoc;

public class Day11Octopus {

    private final boolean active;
    private int state;
    private boolean flashing;
    private boolean flashed;

    public Day11Octopus() {
        this.active = false;
    }

    public Day11Octopus(char state) {
        this.state = state - '0';
        this.active = true;
    }

    public boolean hasFlashed() {
        return flashed;
    }

    public void startStep() {
        if (active) {
            if (flashed) {
                state = 0;
            }
            state++;
            flashing = (state > 9);
            flashed = false;
        }
    }

    public boolean flashCheck() {
        if (flashing) {
            flashed = true;
            flashing = false;
            return true;
        }
        return false;
    }

    public void neighborFlashed() {
        if (active) {
            if (!flashed) {
                state++;
                flashing = (state > 9);
            }
        }
    }

    @Override
    public String toString() {
        return (flashed ? "*" : ("" + state));
    }
}
