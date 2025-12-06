package bob.aoc;

import bob.parser.IntegerListParser;
import bob.parser.ObjectListParser;
import bob.parser.TwoFormatParser;
import bob.util.BaseClass;
import java.util.Iterator;
import java.util.List;

public class Day04b extends BaseClass<TwoFormatParser.Output<List<Integer>, List<Day04Board>>> {

    public static void main(String[] args) {
        new Day04b().run(args, "");
    }

    public Day04b() {
        super(false);
        setParser(new TwoFormatParser<>(new IntegerListParser(),
                new ObjectListParser<>(Day04Board::new, Day04Board::addRow)));
    }

    @Override
    public void solve(TwoFormatParser.Output<List<Integer>, List<Day04Board>> data) {
        List<Integer> toCall = data.first;
        List<Day04Board> boards = data.rest;
        LOG.info("read {} boards, and {} numbers to call", boards.size(), toCall.size());

        int score = 0;
        for (int i = 0; ((i < toCall.size()) && (score == 0)); i++) {
            for (Iterator<Day04Board> it = boards.iterator(); it.hasNext();) {
                Day04Board b = it.next();
                int called = toCall.get(i);
                b.mark(called);
                if (b.isWinner()) {
                    it.remove();
                    if (boards.isEmpty()) {
                        score = b.sumUncalled() * called;
                    }
                }
            }
        }

        LOG.info("Answer is {}", score);
    }
}
