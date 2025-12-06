package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day05b extends BaseClass<List<Day05Pass>> {

    public static void main(String[] args) {
        new Day05b().run(args, "");
    }

    public Day05b() {
        super(false);
        setParser(new ObjectParser<>(Day05Pass::new));
    }

    @Override
    public void solve(List<Day05Pass> passes) {
        LOG.info("read {} tickets", passes.size());

        Set<Integer> ids = passes.stream()
                .map(p -> p.id)
                .collect(Collectors.toSet());
        
        for (Integer id : ids) {
            if ((!ids.contains(id + 1)) && ids.contains(id + 2)) {
                LOG.info("My seat ID is {}", (id + 1));
            }
        }
    }
}
