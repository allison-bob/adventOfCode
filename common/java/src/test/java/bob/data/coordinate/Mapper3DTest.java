package bob.data.coordinate;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

public class Mapper3DTest {

    private static final Coord3D C0 = new Coord3D(1, 2, 3);
    
    public Mapper3DTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testMap() {
        assertAll("mapping",
                // X pointing along +X
                () -> assertEquals(new Coord3D(1, 2, 3), Mapper3D.ENU.map(C0), "ENU"),
                () -> assertEquals(new Coord3D(1, -3, 2), Mapper3D.EDN.map(C0), "EDN"),
                () -> assertEquals(new Coord3D(1, -2, -3), Mapper3D.ESD.map(C0), "ESD"),
                () -> assertEquals(new Coord3D(1, 3, -2), Mapper3D.EUS.map(C0), "EUS"),
                // X pointing along -X
                () -> assertEquals(new Coord3D(-1, 2, -3), Mapper3D.WND.map(C0), "WND"),
                () -> assertEquals(new Coord3D(-1, 3, 2), Mapper3D.WUN.map(C0), "WUN"),
                () -> assertEquals(new Coord3D(-1, -2, 3), Mapper3D.WSU.map(C0), "WSU"),
                () -> assertEquals(new Coord3D(-1, -3, -2), Mapper3D.WDS.map(C0), "WDS"),
                // X pointing along +Y
                () -> assertEquals(new Coord3D(2, -1, 3), Mapper3D.NWU.map(C0), "NWU"),
                () -> assertEquals(new Coord3D(2, -3, -1), Mapper3D.NDW.map(C0), "NDW"),
                () -> assertEquals(new Coord3D(2, 1, -3), Mapper3D.NED.map(C0), "NED"),
                () -> assertEquals(new Coord3D(2, 3, 1), Mapper3D.NUE.map(C0), "NUE"),
                // X pointing along -Y
                () -> assertEquals(new Coord3D(-2, 1, 3), Mapper3D.SEU.map(C0), "SEU"),
                () -> assertEquals(new Coord3D(-2, -3, 1), Mapper3D.SDE.map(C0), "SDE"),
                () -> assertEquals(new Coord3D(-2, -1, -3), Mapper3D.SWD.map(C0), "SWD"),
                () -> assertEquals(new Coord3D(-2, 3, -1), Mapper3D.SUW.map(C0), "SUW"),
                // X pointing along +Z
                () -> assertEquals(new Coord3D(-3, 2, 1), Mapper3D.UNW.map(C0), "UNW"),
                () -> assertEquals(new Coord3D(-3, -1, 2), Mapper3D.UEN.map(C0), "UEN"),
                () -> assertEquals(new Coord3D(-3, -2, -1), Mapper3D.USE.map(C0), "USE"),
                () -> assertEquals(new Coord3D(-3, 1, -2), Mapper3D.UWS.map(C0), "UWS"),
                // X pointing along -Z
                () -> assertEquals(new Coord3D(3, 2, -1), Mapper3D.DSE.map(C0), "DSE"),
                () -> assertEquals(new Coord3D(3, 1, 2), Mapper3D.DWS.map(C0), "DWS"),
                () -> assertEquals(new Coord3D(3, -2, 1), Mapper3D.DNW.map(C0), "DNW"),
                () -> assertEquals(new Coord3D(3, -1, -2), Mapper3D.DEN.map(C0), "DEN")
        );
    }

    @Test
    public void testInverses() {
        List<Executable> tests = new ArrayList<>();
        for (Mapper3D m : Mapper3D.values()) {
            tests.add(() -> testOneInverse(m));
        }
        assertAll("inverses", tests.stream());
    }

    private void testOneInverse(Mapper3D m) {
        Mapper3D i = m.inverse();
        for (Mapper3D u : Mapper3D.values()) {
            if (C0.equals(u.map(m.map(C0)))) {
                assertEquals(i, u, m.name());
            }
        }
    }
}
