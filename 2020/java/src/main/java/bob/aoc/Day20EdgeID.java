package bob.aoc;

import bob.data.coordinate.Mapper2D;

public class Day20EdgeID {

        public final int tilenum;
        public final Mapper2D orientation;
        public final int edgenum;

        public Day20EdgeID(int tilenum, Mapper2D orientation, int edgenum) {
            this.tilenum = tilenum;
            this.orientation = orientation;
            this.edgenum = edgenum;
        }

        @Override
        public String toString() {
            return "{tile=" + tilenum + ",orient=" + orientation + ",edge=" + edgenum + "}";
        }
}
