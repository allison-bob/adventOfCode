package bob.aoc;

import bob.parser.SingleIntegerParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day20b extends BaseClass<List<Integer>> {

    private static final long DECRYPT_KEY = 811589153;

    public static void main(String[] args) {
        new Day20b().run(args, "");
    }

    public Day20b() {
        super(false);
        setParser(new SingleIntegerParser());
    }

    @Override
    public void solve(List<Integer> numbers) {
        LOG.info("read {} numbers", numbers.size());

        List<Day20Entry> current = IntStream.range(0, numbers.size())
                .mapToObj(i -> new Day20Entry(i, (numbers.get(i) * DECRYPT_KEY)))
                .collect(Collectors.toList());

        for (int r = 0; r < 10; r++) {
            for (int i = 0; i < numbers.size(); i++) {
                current = move(current, i);
            }
            LOG.debug("   Result is {}", (isRealData() ? current.size() : current));
        }

        int origpos = numbers.indexOf(0);
        int pos = findOrigIdx(current, origpos);
        LOG.debug("0 is at {},{}: {}", origpos, pos, current.get(pos));
        int x = (pos + 1000) % current.size();
        int y = (pos + 2000) % current.size();
        int z = (pos + 3000) % current.size();
        Day20Entry xval = current.get(x);
        Day20Entry yval = current.get(y);
        Day20Entry zval = current.get(z);

        LOG.info("answer is ({},{},{}): {} + {} + {} = {}", x, y, z, xval, yval, zval,
                (xval.value() + yval.value() + zval.value()));
    }

    private int findOrigIdx(List<Day20Entry> current, int origidx) {
        for (int i = 0; i < current.size(); i++) {
            if (current.get(i).origPos() == origidx) {
                //LOG.debug("   entry to move is {}:{}", i, current.get(i));
                return i;
            }
        }
        throw Assert.failed(null, "Original index not found");
    }

    private List<Day20Entry> move(List<Day20Entry> current, int origidx) {
        //LOG.debug("Moving {} in {}", origidx, (isRealData() ? current.size() : current));

        // Rotate the item to move to the top of the list
        List<Day20Entry> result = rotate(current, findOrigIdx(current, origidx));

        // Grab the item to move
        Day20Entry toMove = result.remove(0);

        // Rotate the list by the value to put the number after the insertion point at the top of the list
        result = rotate(result, toMove.value());

        // Add the item to move to the end
        result.add(toMove);
        //LOG.debug("   Result is {}", (isRealData() ? result.size() : result));

        return result;
    }

    private List<Day20Entry> rotate(List<Day20Entry> in, long dist) {
        int modDist = (int) (dist % in.size());
        if (modDist == 0) {
            return in;
        }
        if (modDist < 0) {
            modDist += in.size();
        }

        List<Day20Entry> result = new ArrayList<>();
        result.addAll(in.subList(modDist, in.size()));
        result.addAll(in.subList(0, modDist));

        return result;
    }
}
