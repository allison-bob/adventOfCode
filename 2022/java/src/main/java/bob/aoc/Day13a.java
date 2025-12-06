package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day13a extends BaseClass<List<Day13Packet>> {

    public static void main(String[] args) {
        new Day13a().run(args, "");
    }

    public Day13a() {
        super(false);
        setParser(new ObjectListParser<>(Day13Packet::new, Day13Packet::addData));
    }

    @Override
    public void solve(List<Day13Packet> data) {
        LOG.info("Read {} packets", data.size());
        LOG.debug("{}", data);
        int sum = 0;
        for (int i = 0; i < data.size(); i++) {
            Day13Packet d = data.get(i);
            boolean v = d.valid();
            LOG.debug("{} packet {} valid = {}", i, d, v);
            //if (data.get(i).valid()) {
            if (v) {
                sum += i + 1;
                LOG.debug("... sum is now {}", sum);
            }
        }

        LOG.info("Answer is {}", sum);
    }
}
