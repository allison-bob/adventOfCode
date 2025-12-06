package bob.parser;

/**
 * Parse input data with an initial part in one format and one or more parts in another format. Its expected that
 * the first parser will expect one part and the second parser will expect multiple parts.
 */
public class TwoFormatParser<A, B> implements PuzzleDataParser<TwoFormatParser.Output<A, B>> {

    public static class Output<A, B> {
        public A first;
        public B rest;

        private Output(A first, B rest) {
            this.first = first;
            this.rest = rest;
        }
    }

    PuzzleDataParser<A> firstParser;
    PuzzleDataParser<B> restParser;

    public TwoFormatParser(PuzzleDataParser<A> firstParser, PuzzleDataParser<B> restParser) {
        this.firstParser = firstParser;
        this.restParser = restParser;
    }

    @Override
    public void open(int partnum) {
        if (partnum == 0) {
            firstParser.open(partnum);
        } else {
            restParser.open(partnum - 1);
        }
    }

    @Override
    public void read(int partnum, String line) {
        if (partnum == 0) {
            firstParser.read(partnum, line);
        } else {
            restParser.read((partnum - 1), line);
        }
    }

    @Override
    public void close(int partnum) {
        if (partnum == 0) {
            firstParser.close(partnum);
        } else {
            restParser.close(partnum - 1);
        }
    }

    @Override
    public Output<A, B> getResult() {
        return new Output<>(firstParser.getResult(), restParser.getResult());
    }
}
