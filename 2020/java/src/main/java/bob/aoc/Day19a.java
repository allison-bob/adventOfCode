package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import bob.parser.TwoFormatParser;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19a extends BaseClass<TwoFormatParser.Output<Map<Integer, Day19Rule>, List<String>>> {

    public static void main(String[] args) {
        new Day19a().run(args, "");
    }

    public Day19a() {
        super(false);
        setParser(new TwoFormatParser<>(
                new LineObjectMapParser<>(Day19Rule::new, Day19Rule::getRuleNum),
                new ObjectParser<>(line -> line)
        ));
    }

    @Override
    public void solve(TwoFormatParser.Output<Map<Integer, Day19Rule>, List<String>> data) {
        Map<Integer, Day19Rule> rulesread = data.first;
        List<String> messages = data.rest;
        LOG.info("read {} rules and {} messages", rulesread.size(), messages.size());
        
        // Convert the rules to regex expressions
        Map<Integer, Pattern> rules = new HashMap<>();
        while (!rulesread.isEmpty()) {
            convertRule(findConvertable(rulesread), rulesread, rules);
        }
        
        // Check the messages
        Pattern patt = rules.get(0);
        long count = messages.stream()
                .peek(m -> LOG.debug("Checking {}", m))
                .filter(m -> patt.matcher(m).matches())
                .peek(m -> LOG.debug("... matches"))
                .count();
        
        LOG.info("valid message count = {}", count);
    }
    
    private void convertRule(int rulenum, Map<Integer, Day19Rule> rulesread, Map<Integer, Pattern> rules) {
        // Convert rule to text
        String regex = rulesread.get(rulenum).getElements().stream()
                .map(r -> r.toString())
                .collect(Collectors.joining());
        if (regex.contains("|")) {
            regex = "(" + regex + ")";
        }
        LOG.debug("rule {} = {}", rulenum, regex);

        // Move the rule to the rule list
        rules.put(rulenum, Pattern.compile(regex));
        rulesread.remove(rulenum);

        // Substitute the regex into other rules
        for (Day19Rule read : rulesread.values()) {
            List<Object> rule = read.getElements();
            for (int i = 0; i < rule.size(); i++) {
                Object bit = rule.get(i);
                if (bit instanceof Integer ival) {
                    if (ival.equals(rulenum)) {
                        rule.set(i, regex);
                    }
                }
            }
        }
    }
    
    private int findConvertable(Map<Integer, Day19Rule> rulesread) {
        for (Integer rulenum : rulesread.keySet()) {
            List<Object> rule = rulesread.get(rulenum).getElements();
            //System.out.println("checking " + rule);
            Optional<Object> found = rule.stream()
                    .filter(r -> r instanceof Integer)
                    .findFirst();
            if (found.isEmpty()) {
                return rulenum;
            }
        }
        throw new IllegalArgumentException("no rules can be converted");
    }
}
