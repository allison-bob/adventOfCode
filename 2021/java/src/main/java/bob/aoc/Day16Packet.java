package bob.aoc;

import bob.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Day16Packet {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final int version;
    private final int type;
    private final long value;
    private final List<Day16Packet> subpackets = new ArrayList<>();
    private int bitct;

    public Day16Packet(Day16BitStream bits, String prefix) {
        version = (int) bits.getBits(3);
        type = (int) bits.getBits(3);
        if (type == 4) {
            bitct = 6;
            long v = 0;
            int flag = 1;
            while (flag == 1) {
                flag = (int) bits.getBits(1);
                v = (v << 4) + bits.getBits(4);
                bitct += 5;
            }
            value = v;
            LOG.debug("{}Type 4 length {}", prefix, bitct);
        } else {
            value = 0;
            int flag = (int) bits.getBits(1);
            if (flag == 0) {
                long bitlen = bits.getBits(15);
                bitct += 22;
                LOG.debug("{}Type {} bit length {}", prefix, type, bitlen);
                while (bitlen > 0) {
                    Day16Packet p = new Day16Packet(bits, prefix + "| ");
                    bitct += p.bitct;
                    bitlen -= p.bitct;
                    subpackets.add(p);
                    LOG.debug("{}... remaining bit length {}", prefix, bitlen);
                }
            } else {
                int pktct = (int) bits.getBits(11);
                bitct += 18;
                LOG.debug("{}Type {} packet count {}", prefix, type, pktct);
                for (int i = 0; i < pktct; i++) {
                    Day16Packet p = new Day16Packet(bits, prefix + ". ");
                    bitct += p.bitct;
                    subpackets.add(p);
                }
            }
        }
    }

    public long evaluate() {
        return switch (type) {
            case 0 -> {
                // Sum of operands
                Assert.that(!subpackets.isEmpty(), "Type 0 needs operands");
                yield subpackets.stream()
                        .mapToLong(Day16Packet::evaluate)
                        .reduce(0, Long::sum);
            }
            case 1 -> {
                // Product of operands
                Assert.that(!subpackets.isEmpty(), "Type 1 needs operands");
                yield subpackets.stream()
                        .mapToLong(Day16Packet::evaluate)
                        .reduce(1, (a, b) -> a * b);
            }
            case 2 -> {
                // Minimum of operands
                Assert.that(!subpackets.isEmpty(), "Type 2 needs operands");
                yield subpackets.stream()
                        .mapToLong(Day16Packet::evaluate)
                        .reduce(Long.MAX_VALUE, Long::min);
            }
            case 3 -> {
                // Maximum of operands
                Assert.that(!subpackets.isEmpty(), "Type 3 needs operands");
                yield subpackets.stream()
                        .mapToLong(Day16Packet::evaluate)
                        .reduce(0, Long::max);
            }
            case 4 -> {
                // Return the value
                yield value;
            }
            case 5 -> {
                // Greater than
                Assert.that((subpackets.size() == 2), "Type 5 needs exactly 2 operands");
                yield (subpackets.get(0).evaluate() > subpackets.get(1).evaluate()) ? 1 : 0;
            }
            case 6 -> {
                // Less than
                Assert.that((subpackets.size() == 2), "Type 6 needs exactly 2 operands");
                yield (subpackets.get(0).evaluate() < subpackets.get(1).evaluate()) ? 1 : 0;
            }
            case 7 -> {
                // Equals
                Assert.that((subpackets.size() == 2), "Type 7 needs exactly 2 operands");
                yield (subpackets.get(0).evaluate() == subpackets.get(1).evaluate()) ? 1 : 0;
            }
            default ->
                throw new RuntimeException("Unexpected packet type " + type);
        };
    }

    public Stream<Day16Packet> packetStream() {
        return Stream.concat(
                Stream.of(this),
                subpackets.stream()
                        .flatMap(Day16Packet::packetStream)
        );
    }

    @Override
    public String toString() {
        return "{" + version + "," + type + ":" + value + ", " + subpackets + "}";
    }
}
