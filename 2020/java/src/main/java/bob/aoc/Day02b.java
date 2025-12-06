package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day02b extends BaseClass<List<Day02Data>> {

    public static void main(String[] args) {
        new Day02b().run(args, "");
    }

    public Day02b() {
        super(false);
        setParser(new ObjectParser<>(Day02Data::new));
    }

    @Override
    public void solve(List<Day02Data> entries) {
        LOG.info("Read {} entries", entries.size());
        
        long count = entries.stream()
                .filter(Day02Data::testB)
                .count();
        LOG.info("valid passwords: {}", count);
    }
}
