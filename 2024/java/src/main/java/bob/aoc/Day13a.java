package bob.aoc;

import bob.parser.ObjectListParser;
import bob.util.BaseClass;
import java.util.List;

public class Day13a extends BaseClass<List<Day13Machine>> {

    public static void main(String[] args) {
        new Day13a().run(args, "");
    }

    public Day13a() {
        super(false);
        setParser(new ObjectListParser<>(Day13Machine::new, Day13Machine::accept));
    }

    @Override
    public void solve(List<Day13Machine> machines) {
        LOG.info("Read {} machines", machines.size());
        LOG.debug(machines.toString());
        
        // Find a solution for each machine
        long total = 0;
        for (Day13Machine m : machines) {
            LOG.debug("Solving {}", m);
            solve(m);
            total += m.cost();
        }
        
        LOG.info("total: {}", total);
    }

    public void solve(Day13Machine m) {
        // We have the following two equations in two unknowns (pa and pb):
        //    1) (ax * pa) + (bx * pb) = px
        //    2) (ay * pa) + (by * pb) = py
        //
        // Scale eq1 by +by and eq2 by -bx:
        //    1) (+by * ax * pa) + (+by * bx * pb) = (+by * px)
        //    2) (-bx * ay * pa) + (-bx * by * pb) = (-bx * py)
        //
        // Add the two equations:
        //    (+by * ax * pa) + (-bx * ay * pa) = (+by * px) + (-bx * py)
        //
        // Solve for pa:
        //    pa * ((by * ax) - (bx * ay)) = (by * px) - (bx * py)
        //    pa = ((by * px) - (bx * py)) / ((by * ax) - (bx * ay))
        long pa_num = ((m.by * m.px) - (m.bx * m.py));
        long pa_den = ((m.by * m.ax) - (m.bx * m.ay));
        System.out.println("Push A: " + pa_num + "/" + pa_den + "="
                + ((1. * pa_num) / pa_den) + ", %=" + (pa_num % pa_den));
        
        // If the remainder of (pa_num/pa_den) is not 0, we have no solution
        if ((pa_num % pa_den) != 0) {
            return;
        }
        long pa = pa_num / pa_den;
        
        // Solve the first equation above for pb:
        //    (ax * pa) + (bx * pb) = px
        //    (bx * pb) = px - (ax * pa)
        //    pb = (px - (ax * pa)) / bx
        long pb_num = m.px - (m.ax * pa);
        long pb_den = m.bx;
        System.out.println("Push B: " + pb_num + "/" + pb_den + "="
                + ((1. * pb_num) / pb_den) + ", %=" + (pb_num % pb_den));
        
        // If the remainder of (pb_num/pb_den) is not 0, we have no solution
        if ((pb_num % pb_den) != 0) {
            return;
        }
        
        // Store the results
        m.pa = pa;
        m.pb = pb_num / pb_den;
    }
}
