package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day04a extends BaseClass<List<Day04Cred>> {

    public static void main(String[] args) {
        new Day04a().run(args, "");
    }

    public Day04a() {
        super(false);
        setParser(new ObjectListParser<>(Day04Cred::new, Day04Cred::add));
    }

    @Override
    public void solve(List<Day04Cred> creds) {
        LOG.info("read {} credentials", creds.size());
        
        long count = creds.stream()
                .filter(Day04Cred::hasAllRequired)
                .count();
        
        LOG.info("There are {} valid credentials", count);
    }
}
