package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;

public class Day05a extends BaseClass<List<Day05Pass>> {

    public static void main(String[] args) {
        new Day05a().run(args, "");
    }

    public Day05a() {
        super(false);
        setParser(new ObjectParser<>(Day05Pass::new));
    }

    @Override
    public void solve(List<Day05Pass> passes) {
        LOG.info("read {} tickets", passes.size());

        long hival = passes.stream()
                .mapToInt(s -> s.id)
                .max().getAsInt();

        LOG.info("Highest seat ID is {}", hival);
    }
}
