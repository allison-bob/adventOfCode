package bob.aoc;

import bob.util.Assert;

public class Day02Data {

        public int lowbound;
        public int highbound;
        public char totest;
        public char[] password;

        public Day02Data(String line) {
            String[] bits = line.split("[- :]");
            Assert.that((bits.length == 5), "input line field count");
            this.lowbound = Integer.parseInt(bits[0]);
            this.highbound = Integer.parseInt(bits[1]);
            this.totest = bits[2].charAt(0);
            this.password = bits[4].toCharArray();
        }

        public boolean testA() {
            int ct = 0;
            for (int i = 0; i < password.length; i++) {
                if (password[i] == totest) {
                    ct++;
                }
            }

            boolean result = ((ct >= lowbound) && (ct <= highbound));

            return result;
        }

        public boolean testB() {
            boolean result = ((password[lowbound - 1] == totest) ^ (password[highbound - 1] == totest));

            return result;
        }

        @Override
        public String toString() {
            return lowbound + ".." + highbound + "(" + totest + ") " + new String(password);
        }
}
