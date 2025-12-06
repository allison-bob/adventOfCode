package bob.aoc;

import java.util.List;
import java.util.stream.Stream;

public class Day02Game {
    
    public static class Draw {
        public int red;
        public int green;
        public int blue;

        public Draw(String line) {
            String[] bits = line.trim().split(", ");
            for (String s : bits) {
                int pos = s.indexOf(" ");
                if (s.endsWith("red")) {
                    red = Integer.parseInt(s.substring(0, pos));
                }
                if (s.endsWith("green")) {
                    green = Integer.parseInt(s.substring(0, pos));
                }
                if (s.endsWith("blue")) {
                    blue = Integer.parseInt(s.substring(0, pos));
                }
            }
        }

        @Override
        public String toString() {
            return "(" + red + "," + green + "," + blue + ")";
        }
    }
    
    public int id;
    public List<Draw> draws;

    public Day02Game(String line) {
        String[] bits = line.split(":");
        id = Integer.parseInt(bits[0].substring(5));
        
        bits = bits[1].split(";");
        draws = Stream.of(bits)
                .map(Draw::new)
                .toList();
        System.out.println("ID: " + id + ", draws: " + draws);
    }
}
