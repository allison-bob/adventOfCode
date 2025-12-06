package bob.aoc;

import java.util.Arrays;
import lombok.ToString;

@ToString
public class Day19Part {

    public long a;
    public long m;
    public long s;
    public long x;

    public Day19Part(String line) {
        String[] bits = line.substring(1).split("[},]");
        for (String b : bits) {
            long val = Long.parseLong(b.substring(2));
            switch (b.charAt(0)) {
                case 'a' -> {
                    a = val;
                }
                case 'm' -> {
                    m = val;
                }
                case 's' -> {
                    s = val;
                }
                case 'x' -> {
                    x = val;
                }
            }
        }
    }

    public long get(char key) {
        return switch (key) {
            case 'a' ->
                a;
            case 'm' ->
                m;
            case 's' ->
                s;
            case 'x' ->
                x;
            default ->
                0;
        };
    }
}
