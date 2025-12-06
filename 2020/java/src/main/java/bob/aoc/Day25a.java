package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.List;
import java.util.stream.Collectors;

public class Day25a extends BaseClass<List<Day25Encrypter>> {

    public static void main(String[] args) {
        new Day25a().run(args, "");
    }

    public Day25a() {
        super(false);
        setParser(new ObjectParser<>(Day25Encrypter::new));
    }

    @Override
    public void solve(List<Day25Encrypter> items) {
        LOG.info("Read {} items", items.size());
        LOG.debug("Items: {}", items);

        // Compute the loop counts
        for (Day25Encrypter e : items) {
            e.findLoopCt();
        }
        LOG.debug("Items: {}", items);

        // Compute the encryption keys
        for (int i = 0; i < items.size(); i++) {
            items.get(i).findEncryptionKey(items.get(items.size() - i - 1).getPublicKey());
        }
        LOG.debug("Items: {}", items);
        
        List<Long> ekeys = items.stream()
                .map(e -> e.getEncrKey())
                .collect(Collectors.toList());
        LOG.info("Encryption keys are {}", ekeys);
    }
}
