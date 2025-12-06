package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day02a extends BaseClass<List<Day02Command>> {

    public static void main(String[] args) {
        new Day02a().run(args, "");
    }

    public Day02a() {
        super(false);
        setParser(new ObjectParser<>(Day02Command::new));
    }

    @Override
    public void solve(List<Day02Command> entries) {
        LOG.info("Read {} entries", entries.size());

        int hpos = 0;
        int vpos = 0;

        for (Day02Command cmd : entries) {
            switch (cmd.getDirection()) {
                case FORWARD -> hpos += cmd.getDistance();
                case DOWN -> vpos += cmd.getDistance();
                case UP -> vpos -= cmd.getDistance();
            }
        }
        System.out.println("Finished at h=" + hpos + ", v=" + vpos);
        System.out.println("Answer is " + (hpos * vpos));
    }
}
