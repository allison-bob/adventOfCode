package bob.aoc;

import bob.parser.LineListParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;

public class Day01a extends BaseClass<List<List<Long>>> {

    public static void main(String[] args) {
        new Day01a().run(args, "");
    }

    public Day01a() {
        super(false);
        setParser(new LineListParser<>(" ", Long::valueOf));
    }

    @Override
    public void solve(List<List<Long>> entries) {
        LOG.info("Read {} entries", entries.size());
        LOG.debug(entries.toString());
        
        int columnCt = entries.get(0).size();
        
        // Convert each column to a list
        List<List<Long>> columns = new ArrayList<>();
        for (int i = 0; i < columnCt; i++) {
            columns.add(new ArrayList<>());
        }
        for (List<Long> e : entries) {
            Assert.that((e.size() == columnCt), "Row contains " + e.size() + " columns instead of " + columnCt);
            for (int i = 0; i < columnCt; i++) {
                columns.get(i).add(e.get(i));
            }
        }
        LOG.debug(columns.toString());
        
        // Sort the columns
        for (List<Long> c : columns) {
            c.sort(null);
        }
        LOG.debug(columns.toString());
        
        // Find the answer
        long answer = 0;
        for (int i = 0; i < entries.size(); i++) {
            answer += Math.abs(columns.get(0).get(i) - columns.get(1).get(i));
        }
        LOG.info("total: {}", answer);
    }
}
