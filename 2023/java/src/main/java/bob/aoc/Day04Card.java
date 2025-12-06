package bob.aoc;

import java.util.ArrayList;
import java.util.List;

public class Day04Card {
    
    public int id;
    public int count = 1;
    public List<Integer> winning = new ArrayList<>();
    public List<Integer> mynums = new ArrayList<>();

    public Day04Card(String line) {
        String[] bits = line.replaceAll("   ", " ").replaceAll("  ", " ").replace(":", "").split(" ");
        id = Integer.parseInt(bits[1]);
        
        List<Integer> dest = winning;
        for (int i = 2; i < bits.length; i++) {
            if (bits[i].equals("|")) {
                dest = mynums;
            } else {
                dest.add(Integer.valueOf(bits[i]));
            }
        }
    }
}
