package bob.aoc;

import lombok.Getter;

@Getter
public class Day25Encrypter {

    private final long publicKey;
    private long loopCt;
    private long encrKey;

    public Day25Encrypter(String line) {
        this.publicKey = Long.parseLong(line);
    }

    public void findLoopCt() {
        long value = 1;
        for (loopCt = 0; value != publicKey; loopCt++) {
            value = processNumber(7, value);
        }
    }

    public void findEncryptionKey(long partnerKey) {
        encrKey = 1;
        for (int i = 0; i < loopCt; i++) {
            encrKey = processNumber(partnerKey, encrKey);
        }
    }

    private long processNumber(long subject, long in) {
        long prod = subject * in;
        //LOG.debug("subj={}, in={}, prod={}", subject, in, prod);
        return prod % 20201227;
    }

    @Override
    public String toString() {
        return "public=" + publicKey + ",loop=" + loopCt + ",encr=" + encrKey;
    }
}
