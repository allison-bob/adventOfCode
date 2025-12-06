package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.parser.ObjectListParser;
import bob.parser.TwoFormatParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Day05a extends BaseClass<TwoFormatParser.Output<Map<String, Day05a.Seeds>, List<Day05Map>>> {
    
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
        new Day05a().run(args, "");
    }

    public Day05a() {
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
        for (int i = 0; i < seeds.nums.size(); i++) {
            long v = seeds.nums.get(i);
            for (int j = 0; j < maps.size(); j++) {
                v = maps.get(j).convert(v);
            }
            minloc = Math.min(minloc, v);
        }
        
        LOG.info("answer is {}", minloc);
    }
}
