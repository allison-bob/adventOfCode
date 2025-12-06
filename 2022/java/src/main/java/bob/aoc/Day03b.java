package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.Assert;
import bob.util.BaseClass;
import java.util.List;

public class Day03b extends BaseClass<List<Day03Sack>> {

    public static void main(String[] args) {
        new Day03b().run(args, "");
    }

    public Day03b() {
        super(false);
        setParser(new ObjectParser<>(Day03Sack::new));
    }

    @Override
    public void solve(List<Day03Sack> sacks) {
        LOG.info("Read {} sacks", sacks.size());
        Assert.that(((sacks.size() % 3) == 0), "Bad sack count");
        
        int sum = 0;
        for (int i = 0; i < sacks.size(); i += 3) {
            char c = findCommon(
                    sacks.get(i + 0).getContent(),
                    sacks.get(i + 1).getContent(),
                    sacks.get(i + 2).getContent()
            );
            sum += Day03Sack.priority(c);
        }
        
        LOG.info("Priority sum is {}", sum);
    }
    
    private char findCommon(char[] s1, char[] s2, char[] s3) {
        for (int i = 0; i < s1.length; i++) {
            for (int j = 0; j < s2.length; j++) {
                for (int k = 0; k < s3.length; k++) {
                    if ((s1[i] == s2[j]) && (s2[j] == s3[k])) {
                        return s1[i];
                    }
                }
            }
        }
        throw Assert.failed(null, "No match found");
    }
}
