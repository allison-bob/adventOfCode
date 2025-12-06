package bob.aoc;

import bob.util.Assert;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day04Cred {

    private static final List<String> REQ_FIELDS = new ArrayList<>(Arrays.asList(
            "byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"
    ));
    private static final List<String> ALLOWED_ECL = new ArrayList<>(Arrays.asList(
            "amb", "blu", "brn", "gry", "grn", "hzl", "oth"
    ));

    Map<String, String> fields = new HashMap<>();

    public void add(String line) {
        String[] bits = line.split("[ :]");
        Assert.that(((bits.length % 2) == 0), "odd number of fields");
        for (int i = 0; i < bits.length; i += 2) {
            fields.put(bits[i], bits[i + 1]);
        }
    }

    public boolean hasAllRequired() {
        return fields.keySet().containsAll(REQ_FIELDS);
    }

    public boolean checkFields() {
        return checkYear("byr", 1920, 2002)
                && checkYear("iyr", 2010, 2020)
                && checkYear("eyr", 2020, 2030)
                && checkHeight()
                && checkHairColor()
                && checkEyeColor()
                && checkPassportID();
    }

    private boolean between(int toTest, int lo, int hi) {
        return ((toTest >= lo) && (toTest <= hi));
    }

    private boolean checkYear(String field, int lo, int hi) {
        String data = fields.get(field);
        if (data.length() != 4) {
            return false;
        }
        int yr = Integer.parseInt(data);
        return between(yr, lo, hi);
    }

    private boolean checkHeight() {
        String data = fields.get("hgt");
        String[] bits = data.split("cm|in");
        int value = Integer.parseInt(bits[0]);
        if (data.endsWith("in")) {
            return between(value, 59, 76);
        } else if (data.endsWith("cm")) {
            return between(value, 150, 193);
        } else {
            return false;
        }
    }

    private boolean checkHairColor() {
        String data = fields.get("hcl");
        return data.matches("#[0-9a-f]{6}");
    }

    private boolean checkEyeColor() {
        String data = fields.get("ecl");
        return ALLOWED_ECL.contains(data);
    }

    private boolean checkPassportID() {
        String data = fields.get("pid");
        return data.matches("[0-9]{9}");
    }
}
