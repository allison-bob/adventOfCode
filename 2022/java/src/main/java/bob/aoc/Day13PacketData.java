package bob.aoc;

import bob.util.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class Day13PacketData implements Comparable<Day13PacketData> {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private Integer intValue;
    private List<Day13PacketData> listValue;

    public Day13PacketData(String line) {
        this(new PushbackInputStream(new ByteArrayInputStream(line.getBytes()), 5));
    }

    public Day13PacketData(PushbackInputStream data) {
        //LOG.debug(">>>> Start");
        try {
            int read = data.read();
            while (read > -1) {
                //LOG.debug("Read {}", String.valueOf((char) read));
                switch (read) {
                    case '[',',' -> {
                        if (listValue == null) {
                            listValue = new ArrayList<>();
                        }
                        Day13PacketData pd = new Day13PacketData(data);
                        if ((pd.getIntValue() == null) && (pd.getListValue() == null)) {
                            return;
                        } else {
                            listValue.add(pd);
                        }
                        //LOG.debug("list now {}", listValue);
                    }
                    case ']' -> {
                        return;
                    }
                    default -> {
                        int v = 0;
                        while ((read >= (int) '0') && (read <= (int) '9')) {
                            v = (10 * v) + (read - '0');
                            read = data.read();
                        }
                        //LOG.debug("   end of int, read is {}", String.valueOf((char) read));
                        data.unread(read);
                        intValue = v;
                        //LOG.debug("int now {}", intValue);
                        return;
                    }
                }
                read = data.read();
            }
        } catch (IOException ex) {
            throw Assert.failed(ex, "Error parsing data");
        } finally {
            //LOG.debug("<<<< End");
        }
    }

    @Override
    public int compareTo(Day13PacketData that) {
        if ((this.intValue != null) && (that.intValue != null)) {
            //LOG.debug("Comparing ints {} to {}: {}", this.intValue, that.intValue, (this.intValue - that.intValue));
            return (this.intValue - that.intValue);
        }
        if ((this.listValue != null) && (that.listValue != null)) {
            //LOG.debug("Comparing lists {} to {}", this.listValue, that.listValue);
            for (int i = 0; i < Math.min(this.listValue.size(), that.listValue.size()); i++) {
                int c = this.listValue.get(i).compareTo(that.listValue.get(i));
                if (c != 0) {
                    return c;
                }
            }
            //LOG.debug("Comparing list sizes {} to {}: {}", this.listValue.size(), that.listValue.size(),
            //        (this.listValue.size() - that.listValue.size()));
            return (this.listValue.size() - that.listValue.size());
        }
        if (this.intValue != null) {
            //LOG.debug("Converting int {} to list", this.intValue);
            Day13PacketData newpd = new Day13PacketData("[" + this.intValue + "]");
            return newpd.compareTo(that);
        }
        if (that.intValue != null) {
            //LOG.debug("Converting int {} to list", that.intValue);
            Day13PacketData newpd = new Day13PacketData("[" + that.intValue + "]");
            return compareTo(newpd);
        }
        return 0;
    }

    @Override
    public String toString() {
        if (intValue != null) {
            return "" + intValue;
        }
        if (listValue != null) {
            return "" + listValue;
        }
        return "";
    }
}
