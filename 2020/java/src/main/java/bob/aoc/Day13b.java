package bob.aoc;

import bob.algorithm.ChineseRemainder;
import bob.parser.ObjectListParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.List;

public class Day13b extends BaseClass<List<Day13Data>> {

    public static void main(String[] args) {
        new Day13b().run(args, "");
    }

    public Day13b() {
        super(false);
        setParser(new ObjectListParser<>(Day13Data::new, Day13Data::add));
    }

    @Override
    public void solve(List<Day13Data> data) {
        Day13Data first = data.get(0);
        int when = first.getWhen();
        List<Integer> ids = first.getIds();
        LOG.info("read {} ids", ids.size());
        LOG.debug("when = {}, IDs = {}", when, ids);
        Assert.that((ids.size() > 1), "Not enough IDs");

        // Use Chinese Remainder Theorem to find the correct answer
        ChineseRemainder cr = new ChineseRemainder();
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) > 0) {
                cr.addCongruence((ids.get(i) - i), ids.get(i));
            }
        }
        long answer = cr.solve();
        LOG.debug("Inputs: {}", cr.congruences);
        LOG.debug("moduli product: {}", cr.modproduct);

        LOG.info("answer = {}", answer);
    }
}
