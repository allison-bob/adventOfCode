package bob.aoc;

import bob.parser.LineObjectMapParser;
import bob.parser.ObjectParser;
import bob.util.BaseClass;
import bob.parser.TwoFormatParser;
import bob.util.Assert;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day19b extends BaseClass<TwoFormatParser.Output<Map<Integer, Day19Rule>, List<String>>> {

    public static void main(String[] args) {
        new Day19b().run(args, "");
    }

    public Day19b() {
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
        
        // Since rules 8 and 11 are being adjusted to have loops, remove them (and 0) from conversion
        rulesread.remove(0);   // 0: 8 11
        rulesread.remove(8);   // 8: 42
        rulesread.remove(11);  // 11: 42 31

        // Convert the rules to regex expressions
        Map<Integer, Pattern> rules = new HashMap<>();
        while (!rulesread.isEmpty()) {
            convertRule(findConvertable(rulesread), rulesread, rules);
        }
        
        // Check the messages
        long count = messages.stream()
                .peek(m -> LOG.debug("Checking {}", m))
                .filter(m -> matches(m, rules))
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
        throw Assert.failed(null, "no rules can be converted");
    }
    
    private boolean matches(String msg, Map<Integer, Pattern> rules) {
        // Since all valid values are eight chars long, valid values must be a multiple of eight chars long
        if ((msg.length() % 8) != 0) {
            return false;
        }

        // Count the number of initial matches to rule 42
        int pos = 0;
        Pattern p42 = rules.get(42);
        int m42 = 0;
        while (p42.matcher(msg.substring(pos, pos + 8)).matches()) {
            pos += 8;
            if (pos >= msg.length()) {
                // Reached end of string, no 31 matches
                return false;
            }
            m42++;
        }
        LOG.debug("42 matches = {}", m42);
        
        // Count the number of following matches to rule 31
        Pattern p31 = rules.get(31);
        int m31 = 0;
        while (pos < msg.length()) {
            if (!p31.matcher(msg.substring(pos, pos + 8)).matches()) {
                // Segment didn't match, so line not valid
                return false;
            }
            pos += 8;
            m31++;
        }
        LOG.debug("31 matches = {}", m31);
        
        return (m42 > m31);
    }
}
