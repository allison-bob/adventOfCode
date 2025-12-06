package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day16b extends BaseClass<List<Day16BitStream>> {

    public static void main(String[] args) {
        new Day16b().run(args, "a");
    }

    public Day16b() {
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
            long result = pkt.evaluate();
            LOG.info("Packet evaluates to {}", result);
        }
    }
}
