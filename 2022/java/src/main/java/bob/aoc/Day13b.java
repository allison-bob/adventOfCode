package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.TreeSet;

public class Day13b extends BaseClass<List<Day13Packet>> {

    public static void main(String[] args) {
        new Day13b().run(args, "");
    }

    public Day13b() {
        super(false);
        setParser(new ObjectListParser<>(Day13Packet::new, Day13Packet::addData));
    }

    @Override
    public void solve(List<Day13Packet> data) {
        LOG.info("Read {} packets", data.size());
        //LOG.debug("{}", data);

        TreeSet<Day13PacketData> s = new TreeSet<>();
        for (Day13Packet d : data) {
            s.add(d.getLeft());
            s.add(d.getRight());
        }
        s.add(new Day13PacketData("[[2]]"));
        s.add(new Day13PacketData("[[6]]"));
        int ssize = s.size();
        LOG.debug("Assembled packet data has {} entries", s.size());

        int answer = 1;
        for (int i = 0; i < ssize; i++) {
            Day13PacketData pd = s.pollFirst();
            if ((pd.getListValue() != null) && (!pd.getListValue().isEmpty()) && (pd.getListValue().size() == 1)) {
                Day13PacketData pd1 = pd.getListValue().get(0);
                if ((pd1.getListValue() != null) && (!pd1.getListValue().isEmpty())
                        && (pd1.getListValue().size() == 1)) {
                    Day13PacketData pd2 = pd1.getListValue().get(0);
                    LOG.debug("{}: First item is {}", i, pd2);
                    if (pd2.getIntValue() != null) {
                        if ((pd2.getIntValue() == 2) || (pd2.getIntValue() == 6)) {
                            answer *= i + 1;
                            LOG.debug("  ... answer now {}", answer);
                        }
                    }
                }
            }
        }

        LOG.info("Answer is {}", answer);
    }
}
