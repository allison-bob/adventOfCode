package bob.aoc;

import bob.data.coordinate.Coord3D;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day22Region {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final Coord3D lo;
    private final Coord3D hi;
    private boolean initAreaOnly;

    public Day22Region(Day22Instruction instr) {
        this.lo = new Coord3D(instr.getXlo(), instr.getYlo(), instr.getZlo());
        this.hi = new Coord3D(instr.getXhi(), instr.getYhi(), instr.getZhi());
    }

    public Day22Region(Coord3D lo, int xhi, int yhi, int zhi) {
        this.lo = lo;
        this.hi = new Coord3D(xhi, yhi, zhi);
    }

    public Day22Region(int xlo, int ylo, int zlo, Coord3D hi) {
        this.lo = new Coord3D(xlo, ylo, zlo);
        this.hi = hi;
    }

    public Day22Region(int xlo, int ylo, int zlo, int xhi, int yhi, int zhi) {
        this.lo = new Coord3D(xlo, ylo, zlo);
        this.hi = new Coord3D(xhi, yhi, zhi);
    }

    public Day22Region(Coord3D lo, Coord3D hi) {
        this.lo = lo;
        this.hi = hi;
    }

    public void onlyInitArea() {
        initAreaOnly = true;
    }

    public boolean overlaps(Day22Region toTest) {
        boolean result = true;
        result &= (this.hi.getX() >= toTest.lo.getX());
        result &= (this.lo.getX() <= toTest.hi.getX());
        result &= (this.hi.getY() >= toTest.lo.getY());
        result &= (this.lo.getY() <= toTest.hi.getY());
        result &= (this.hi.getZ() >= toTest.lo.getZ());
        result &= (this.lo.getZ() <= toTest.hi.getZ());
        return result;
    }

    public long size() {
        Coord3D sizeLo = lo;
        Coord3D sizeHi = hi;
        LOG.debug("Actual bounds: {} - {}", lo, hi);
        if (initAreaOnly) {
            if ((hi.getX() < -50) || (hi.getY() < -50) || (hi.getZ() < -50) ||
                    (lo.getX() > 50) || (lo.getY() > 50) || (lo.getZ() > 50)) {
                return 0;
            }
            sizeLo = new Coord3D(Math.max(-50, lo.getX()), Math.max(-50, lo.getY()), Math.max(-50, lo.getZ()));
            sizeHi = new Coord3D(Math.min(50, hi.getX()), Math.min(50, hi.getY()), Math.min(50, hi.getZ()));
            LOG.debug("Adjusted bounds: {} - {}", sizeLo, sizeHi);
        }
        return ((Math.abs(sizeHi.getX() - sizeLo.getX()) + 1L)
                * (Math.abs(sizeHi.getY() - sizeLo.getY()) + 1L)
                * (Math.abs(sizeHi.getZ() - sizeLo.getZ()) + 1L));
    }

    // Split this region based on the new region
    public Stream<Day22Region> split(Day22Region newreg) {
        // Split along the X boundaries of the new region
        Map<Day22Region, Integer> xSplits = new HashMap<>();
        Day22Region r = this;
        LOG.debug("X split {} - {}", this, newreg);

        if (r.lo.getX() < newreg.lo.getX()) {
            Day22Region x1 = new Day22Region(r.lo, (newreg.lo.getX() - 1), r.hi.getY(), r.hi.getZ());
            LOG.debug("   low split: {}", x1);
            xSplits.put(x1, 0);
        }

        Day22Region x2 = new Day22Region(
                Math.max(r.lo.getX(), newreg.lo.getX()), r.lo.getY(), r.lo.getZ(),
                Math.min(r.hi.getX(), newreg.hi.getX()), r.hi.getY(), r.hi.getZ()
        );
        LOG.debug("   mid split: {}", x2);
        xSplits.put(x2, 1);

        if (r.hi.getX() > newreg.hi.getX()) {
            Day22Region x3 = new Day22Region((newreg.hi.getX() + 1), r.lo.getY(), r.lo.getZ(), r.hi);
            LOG.debug("   hi split: {}", x3);
            xSplits.put(x3, 0);
        }

        // Split along the Y boundaries of the new region
        Map<Day22Region, Integer> ySplits = new HashMap<>();
        for (Map.Entry<Day22Region, Integer> e : xSplits.entrySet()) {
            r = e.getKey();
            LOG.debug("Y split {} - {}", r, newreg);

            if (r.lo.getY() < newreg.lo.getY()) {
                Day22Region y1 = new Day22Region(r.lo, r.hi.getX(), (newreg.lo.getY() - 1), r.hi.getZ());
                LOG.debug("   low split: {}", y1);
                ySplits.put(y1, 0);
            }

            Day22Region y2 = new Day22Region(
                    r.lo.getX(), Math.max(r.lo.getY(), newreg.lo.getY()), r.lo.getZ(),
                    r.hi.getX(), Math.min(r.hi.getY(), newreg.hi.getY()), r.hi.getZ()
            );
            LOG.debug("   mid split: {}", y2);
            ySplits.put(y2, (e.getValue() + 1));

            if (r.hi.getY() > newreg.hi.getY()) {
                Day22Region y3 = new Day22Region(r.lo.getX(), (newreg.hi.getY() + 1), r.lo.getZ(), r.hi);
                LOG.debug("   hi split: {}", y3);
                ySplits.put(y3, 0);
            }
        }

        // Split along the Z boundaries of the new region
        Map<Day22Region, Integer> zSplits = new HashMap<>();
        for (Map.Entry<Day22Region, Integer> e : ySplits.entrySet()) {
            r = e.getKey();
            LOG.debug("Z split {} - {}", r, newreg);

            if (r.lo.getZ() < newreg.lo.getZ()) {
                Day22Region z1 = new Day22Region(r.lo, r.hi.getX(), r.hi.getY(), (newreg.lo.getZ() - 1));
                LOG.debug("   low split: {}", z1);
                zSplits.put(z1, 0);
            }

            Day22Region z2 = new Day22Region(
                    r.lo.getX(), r.lo.getY(), Math.max(r.lo.getZ(), newreg.lo.getZ()),
                    r.hi.getX(), r.hi.getY(), Math.min(r.hi.getZ(), newreg.hi.getZ())
            );
            LOG.debug("   mid split: {}", z2);
            zSplits.put(z2, (e.getValue() + 1));

            if (r.hi.getZ() > newreg.hi.getZ()) {
                Day22Region z3 = new Day22Region(r.lo.getX(), r.lo.getY(), (newreg.hi.getZ() + 1), r.hi);
                LOG.debug("   hi split: {}", z3);
                zSplits.put(z3, 0);
            }
        }

        // If any split was in the center in all three splits, it should be the new region; remove it from the set
        Set<Day22Region> retval = zSplits.entrySet().stream()
                .filter(e -> e.getValue() < 3)
                .map(e -> e.getKey())
                .collect(Collectors.toSet());

        return retval.stream();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.lo);
        hash = 67 * hash + Objects.hashCode(this.hi);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Day22Region other = (Day22Region) obj;
        if (!Objects.equals(this.lo, other.lo)) {
            return false;
        }
        return Objects.equals(this.hi, other.hi);
    }

    @Override
    public String toString() {
        return "[" + lo + "," + hi + "]";
    }
}
