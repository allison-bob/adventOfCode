package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.parser.ObjectListParser;
import bob.parser.TwoFormatParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day05b extends BaseClass<TwoFormatParser.Output<Map<String, Day05b.Seeds>, List<Day05Map>>> {

    public static class Seeds {

        public String id;
        public List<Long> nums = new ArrayList<>();

        public Seeds(String line) {
            String[] bits = line.split(" ");
            id = bits[0];
            for (int i = 1; i < bits.length; i++) {
                nums.add(Long.valueOf(bits[i]));
            }
        }
    }

    public static void main(String[] args) {
        new Day05b().run(args, "");
    }

    public Day05b() {
        super(false);
        setParser(new TwoFormatParser<>(
                new LineObjectMapParser<>(line -> new Seeds(line), s -> s.id),
                new ObjectListParser<>(Day05Map::new, Day05Map::addLine)
        ));
    }

    @Override
    public void solve(TwoFormatParser.Output<Map<String, Seeds>, List<Day05Map>> data) {
        Seeds seeds = data.first.get("seeds:");
        List<Day05Map> maps = data.rest;
        LOG.info("Read {} seeds, {} maps", seeds.nums.size(), maps.size());

        long minloc = Long.MAX_VALUE;
        for (int i = 0; i < seeds.nums.size(); i += 2) {
            long s = seeds.nums.get(i);
            for (long j = 0; j < seeds.nums.get(i + 1); j++) {
                long v = s + j;
                for (int k = 0; k < maps.size(); k++) {
                    v = maps.get(k).convert(v);
                }
                minloc = Math.min(minloc, v);
            }
        }

        LOG.info("answer is {}", minloc);
    }
}
