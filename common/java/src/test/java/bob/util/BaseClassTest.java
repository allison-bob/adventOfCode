package bob.util;

import bob.parser.PuzzleDataParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import lombok.Getter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class BaseClassTest {

    @Mock
    private PuzzleDataParser parserMock;
    
    private Day99z target;

    @Getter
    public class Day99z extends BaseClass {

        private String usedDatadir;
        private String usedDaynum;
        private String usedInputSuffix;
        private boolean solveCalled;
        private String usedFormat;
        private Object[] usedArgs;

        public Day99z(PuzzleDataParser parser) {
            super(true);
            setParser(parser);
        }

        @Override
        BufferedReader getPuzzleDataReader(String datadir, String daynum, String inputSuffix) throws IOException {
            usedDatadir = datadir;
            usedDaynum = daynum;
            usedInputSuffix = inputSuffix;
            return new BufferedReader(new StringReader("test\nthis\n\nout"));
        }

        @Override
        public void solve(Object o) {
            solveCalled = true;
        }
    }

    public BaseClassTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        target = new Day99z(parserMock);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testRunEmpty() {
        String[] args = new String[] {};
        target.run(args, "%");
        
        InOrder inOrder = inOrder(parserMock);
        inOrder.verify(parserMock).open(0);
        inOrder.verify(parserMock).read(0, "test");
        inOrder.verify(parserMock).read(0, "this");
        inOrder.verify(parserMock).close(0);
        inOrder.verify(parserMock).open(1);
        inOrder.verify(parserMock).read(1, "out");
        inOrder.verify(parserMock).close(1);
        inOrder.verify(parserMock).getResult();
        verifyNoMoreInteractions(parserMock);
        
        assertEquals("sample", target.getUsedDatadir(), "Wrong data directory");
        assertEquals("99", target.getUsedDaynum(), "Wrong day number");
        assertEquals("%", target.getUsedInputSuffix(), "Wrong input suffix");
        assertFalse(target.isRealData(), "real data used");
        assertTrue(target.isSolveCalled(), "solve not called");
    }

    @Test
    public void testRunTest() {
        String[] args = new String[] {"test"};
        target.run(args, "%");
        
        InOrder inOrder = inOrder(parserMock);
        inOrder.verify(parserMock).open(0);
        inOrder.verify(parserMock).read(0, "test");
        inOrder.verify(parserMock).read(0, "this");
        inOrder.verify(parserMock).close(0);
        inOrder.verify(parserMock).open(1);
        inOrder.verify(parserMock).read(1, "out");
        inOrder.verify(parserMock).close(1);
        inOrder.verify(parserMock).getResult();
        verifyNoMoreInteractions(parserMock);
        
        assertEquals("test", target.getUsedDatadir(), "Wrong data directory");
        assertEquals("99", target.getUsedDaynum(), "Wrong day number");
        assertEquals("%", target.getUsedInputSuffix(), "Wrong input suffix");
        assertFalse(target.isRealData(), "real data used");
        assertTrue(target.isSolveCalled(), "solve not called");
    }

    @Test
    public void testRunSample() {
        String[] args = new String[] {"sample"};
        target.run(args, "%");
        
        InOrder inOrder = inOrder(parserMock);
        inOrder.verify(parserMock).open(0);
        inOrder.verify(parserMock).read(0, "test");
        inOrder.verify(parserMock).read(0, "this");
        inOrder.verify(parserMock).close(0);
        inOrder.verify(parserMock).open(1);
        inOrder.verify(parserMock).read(1, "out");
        inOrder.verify(parserMock).close(1);
        inOrder.verify(parserMock).getResult();
        verifyNoMoreInteractions(parserMock);
        
        assertEquals("sample", target.getUsedDatadir(), "Wrong data directory");
        assertEquals("99", target.getUsedDaynum(), "Wrong day number");
        assertEquals("%", target.getUsedInputSuffix(), "Wrong input suffix");
        assertFalse(target.isRealData(), "real data used");
        assertTrue(target.isSolveCalled(), "solve not called");
    }

    @Test
    public void testRunReal() {
        String[] args = new String[] {"real"};
        target.run(args, "%");
        
        InOrder inOrder = inOrder(parserMock);
        inOrder.verify(parserMock).open(0);
        inOrder.verify(parserMock).read(0, "test");
        inOrder.verify(parserMock).read(0, "this");
        inOrder.verify(parserMock).close(0);
        inOrder.verify(parserMock).open(1);
        inOrder.verify(parserMock).read(1, "out");
        inOrder.verify(parserMock).close(1);
        inOrder.verify(parserMock).getResult();
        verifyNoMoreInteractions(parserMock);
        
        assertEquals("real", target.getUsedDatadir(), "Wrong data directory");
        assertEquals("99", target.getUsedDaynum(), "Wrong day number");
        assertEquals("", target.getUsedInputSuffix(), "Wrong input suffix");
        assertTrue(target.isRealData(), "real data used");
        assertTrue(target.isSolveCalled(), "solve not called");
    }
}
