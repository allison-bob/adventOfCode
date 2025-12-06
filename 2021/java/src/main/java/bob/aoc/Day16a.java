package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day16a extends BaseClass<List<Day16BitStream>> {

    public static void main(String[] args) {
        new Day16a().run(args, "");
    }

    public Day16a() {
        super(false);
        setParser(new ObjectParser<>(Day16BitStream::new));
    }

    @Override
    public void solve(List<Day16BitStream> bits) {
        LOG.info("Read {} streams", bits.size());
        
        for (Day16BitStream data : bits) {
            Day16Packet pkt = new Day16Packet(data, "");
            LOG.debug("Packet: {}", pkt);
            
            // Get the sum of the packet versions
            int result = pkt.packetStream()
                    .mapToInt(Day16Packet::getVersion)
                    .sum();
            LOG.info("Version sum is {}", result);
        }
    }
}
