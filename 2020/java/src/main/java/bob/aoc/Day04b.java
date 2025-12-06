package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day04b extends BaseClass<List<Day04Cred>> {

    public static void main(String[] args) {
        new Day04b().run(args, "");
    }

    public Day04b() {
        super(false);
        setParser(new ObjectListParser<>(Day04Cred::new, Day04Cred::add));
    }

    @Override
    public void solve(List<Day04Cred> creds) {
        LOG.info("read {} credentials", creds.size());
        
        long count = creds.stream()
                .filter(Day04Cred::hasAllRequired)
                .filter(Day04Cred::checkFields)
                .count();
        
        LOG.info("There are {} valid credentials", count);
    }
}
