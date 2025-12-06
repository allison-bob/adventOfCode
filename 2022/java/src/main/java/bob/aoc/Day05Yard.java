package bob.aoc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class Day05Yard {

    private final List<Deque<String>> stacks = new ArrayList<>();

    public Day05Yard() {
        for (int i = 0; i < 11; i++) {
            stacks.add(new ArrayDeque<>());
        }
    }

    public void add(String line) {
        if (line.indexOf('[') > -1) {
            for (int i = 0; i < 10; i++) {
                int charPos = (4 * i) + 1;
                if (line.length() > charPos) {
                    if (line.charAt(charPos - 1) == '[') {
                        stacks.get(i+1).addLast(line.substring(charPos, (charPos + 1)));
                    }
                }
            }
        }
    }
    
    public void move(int from, int to) {
        stacks.get(to).addFirst(stacks.get(from).poll());
    }
    
    public void moveStack(int count, int from, int to) {
        String[] moving = new String[count];
        for (int i = 0; i < count; i++) {
            moving[i] = stacks.get(from).poll();
        }
        for (int i = 0; i < count; i++) {
            stacks.get(to).addFirst(moving[count - i - 1]);
        }
    }
    
    public String tops() {
        StringBuilder sb = new StringBuilder();
        for (Deque<String> stack : stacks) {
            if (!stack.isEmpty()) {
                sb.append(stack.getFirst());
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Deque<String> stack : stacks) {
            if (!stack.isEmpty()) {
                sb.append("\n").append(stack.stream().collect(Collectors.joining(",")));
            }
        }
        return sb.toString();
    }
}
