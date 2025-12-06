package bob.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day12Row {

    public List<Day12Spring> springs;
    public List<Integer> counts;

    // Initial constructor
    public Day12Row(String line) {
        String[] bits = line.split(" ");
        
        springs = new ArrayList<>();
        for (int i = 0; i < bits[0].length(); i++) {
            springs.add(Day12Spring.byChar(bits[0].charAt(i)));
        }
        
        String[] pieces = bits[1].split(",");
        counts = new ArrayList<>();
        for (int i = 0; i < pieces.length; i++) {
            counts.add(Integer.valueOf(pieces[i]));
        }
    }
    
    // Constructor for a recursion step
    public Day12Row(List<Day12Spring> springs, int toReplace, Day12Spring repl, List<Integer> counts) {
        this.springs = new ArrayList<>(springs);
        this.springs.remove(toReplace);
        this.springs.add(toReplace, repl);
        this.counts = counts;
    }
    
    // Constructor for an adjustment
    public Day12Row(List<Day12Spring> springs, List<Integer> counts) {
        this.springs = new ArrayList<>(springs);
        this.counts = counts;
    }
    
    public String makeSpringLine() {
        String line = springs.stream()
                .map(Day12Spring::getSymbol)
                .map(c -> Character.toString(c))
                .collect(Collectors.joining());
        while (line.startsWith(".")) {
            line = line.substring(1);
        }
        return line;
    }
    
    public String[] currGroups() {
        return makeSpringLine().split("[.]+");
    }
    
    public List<Integer> currCounts() {
        return Arrays.stream(currGroups())
                .map(String::length)
                .toList();
    }
    
    public boolean countCheck() {
        return currCounts().equals(counts);
    }
    
    public void unfold() {
        List<Day12Spring> newsprings = new ArrayList<>();
        List<Integer> newcounts = new ArrayList<>();
        
        for (int i = 0; i < 4; i++) {
            newsprings.addAll(springs);
            newsprings.add(Day12Spring.UNKNOWN);
            newcounts.addAll(counts);
        }
        
        newsprings.addAll(springs);
        newcounts.addAll(counts);
        
        springs = newsprings;
        counts = newcounts;
    }

    @Override
    public String toString() {
        return "(" + makeSpringLine() + ":" + counts + ")";
    }
}
