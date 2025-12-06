package bob.aoc;

import bob.parser.ObjectParser;
import bob.util.BaseClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day15b extends BaseClass<List<List<String>>> {

    private enum Op {
        ADD, REMOVE;
    }

    private static class Lens {

        public String label;
        public long hash;
        public Op op;
        public int focalLen;
    }

    private List<Lens>[] boxes;

    public static void main(String[] args) {
        new Day15b().run(args, "");
    }

    public Day15b() {
        super(false);
        setParser(new ObjectParser<>(this::parseLine));
    }

    private List<String> parseLine(String line) {
        String[] bits = line.split(",");
        return Arrays.asList(bits);
    }

    @Override
    public void solve(List<List<String>> data) {
        List<String> entries = data.get(0);
        LOG.info("Read {} entries", entries.size());

        // Initialize the lens boxes
        boxes = new List[256];
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new ArrayList<>();
        }

        // Parse the instructions
        List<Lens> instrs = entries.stream()
                .map(this::parseEntry)
                .toList();

        // Execute the instructions
        for (Lens instr : instrs) {
            updateBoxes(instr);
            //dumpBoxes();
        }

        long answer = 0;
        for (int i = 0; i < boxes.length; i++) {
            List<Lens> box = boxes[i];
            for (int j = 0; j < box.size(); j++) {
                Lens lens = box.get(j);
                answer += (i + 1) * (j + 1) * lens.focalLen;
            }
        }
        LOG.info("total: {}", answer);
    }
    
    private void dumpBoxes() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boxes.length; i++) {
            List<Lens> box = boxes[i];
            if (!box.isEmpty()) {
                sb.append("\nBox ").append(i).append(":");
                box.forEach(l -> sb.append(" [").append(l.label).append(" ").append(l.focalLen).append("]"));
            }
        }
        LOG.info(sb.toString());
    }

    private void updateBoxes(Lens instr) {
        List<Lens> box = boxes[(int) instr.hash];
        int pos = boxIndexOf(box, instr.label);
        switch (instr.op) {
            case REMOVE:
                if (pos > -1) {
                    box.remove(pos);
                }
                break;
            case ADD:
                if (pos > -1) {
                    box.remove(pos);
                    box.add(pos, instr);
                } else {
                    box.add(instr);
                }
                break;
        }
    }

    private int boxIndexOf(List<Lens> box, String label) {
        for (int i = 0; i < box.size(); i++) {
            if (box.get(i).label.equals(label)) {
                return i;
            }
        }
        return -1;
    }

    private Lens parseEntry(String entry) {
        Lens result = new Lens();
        if (entry.endsWith("-")) {
            result.label = entry.substring(0, (entry.length() - 1));
            result.hash = hash(result.label);
            result.op = Op.REMOVE;
        } else {
            result.label = entry.substring(0, (entry.length() - 2));
            result.hash = hash(result.label);
            result.op = Op.ADD;
            result.focalLen = Integer.parseInt(entry.substring((entry.length() - 1)));
        }
        return result;
    }

    private long hash(String in) {
        int out = 0;
        for (char c : in.toCharArray()) {
            out += c;
            out *= 17;
            out = out % 256;
        }
        return out;
    }
}
