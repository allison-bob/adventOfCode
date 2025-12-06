package bob.data.grid;

import java.util.List;

public class GridTestBase {
    
    static final List<GridTestEnum>[] testRows = new List[9];
    static {
        GridTestEnum[] values = GridTestEnum.values();
        for (int i = 0; i < testRows.length; i++) {
            int j = 3 * i;
            testRows[i] = List.of(values[j+0], values[j+1], values[j+2]);
        }
    }

    static final List<GridTestEnum>[][] testRows3D = new List[][] {
        new List[] {testRows[0], testRows[1], testRows[2]},
        new List[] {testRows[3], testRows[4], testRows[5]},
        new List[] {testRows[6], testRows[7], testRows[8]}
    };
    static final List<GridTestEnum>[] testRows2D = testRows3D[0];

    Grid3D<GridTestEnum> loadGrid3() {
        Grid3D<GridTestEnum> retval = new Grid3D<>();
        retval.getSubgrids().clear();
        for (List<GridTestEnum>[] rows : testRows3D) {
            retval.getSubgrids().add(loadGrid2(rows));
        }
        return retval;
    }

    Grid2D<GridTestEnum> loadGrid2(List<GridTestEnum>... rows) {
        Grid2D<GridTestEnum> retval = new Grid2D<>();
        retval.getSubgrids().clear();
        for (List<GridTestEnum> row : rows) {
            retval.getSubgrids().add(loadGrid1(row));
        }
        return retval;
    }

    Grid1D<GridTestEnum> loadGrid1(List<GridTestEnum> row) {
        Grid1D<GridTestEnum> retval = new Grid1D<>();
        retval.getPoints().clear();
        retval.getPoints().addAll(row);
        return retval;
    }

    Grid3D<GridTestEnum> getGrid3(Grid4D<GridTestEnum> src, int coord) {
        return src.getSubgrids().get(coord);
    }

    Grid2D<GridTestEnum> getGrid2(Grid3D<GridTestEnum> src, int coord) {
        return src.getSubgrids().get(coord);
    }

    Grid1D<GridTestEnum> getGrid1(Grid2D<GridTestEnum> src, int coord) {
        return src.getSubgrids().get(coord);
    }

    GridTestEnum getGrid0(Grid1D<GridTestEnum> src, int coord) {
        return src.getPoints().get(coord);
    }

    int getGrid4Size(Grid4D<GridTestEnum> src) {
        return src.getSubgrids().size();
    }

    int getGrid3Size(Grid3D<GridTestEnum> src) {
        return src.getSubgrids().size();
    }

    int getGrid2Size(Grid2D<GridTestEnum> src) {
        return src.getSubgrids().size();
    }

    int getGrid1Size(Grid1D<GridTestEnum> src) {
        return src.getPoints().size();
    }
}
