package bob.aoc;

import bob.parser.IntegerListParser;
import bob.parser.ObjectListParser;
import bob.parser.TwoFormatParser;
import bob.util.BaseClass;
import java.util.List;

public class Day04a extends BaseClass<TwoFormatParser.Output<List<Integer>, List<Day04Board>>> {

    public static void main(String[] args) {
        new Day04a().run(args, "");
    }

    public Day04a() {
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
            for (Day04Board b : boards) {
                int called = toCall.get(i);
                b.mark(called);
                if (b.isWinner()) {
                    score = b.sumUncalled() * called;
                }
            }
        }

        LOG.info("Answer is {}", score);
    }
}
