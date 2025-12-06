package bob.data;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CPUTest {
    
    private static class Op implements CPU.Operation {
        
        public int execCt;

        @Override
        public void execute(CPU cpu) {
            execCt++;
            cpu.setPc(cpu.getPc() + 1);
        }
    }
    
    List<Op> program;
    private Op op0;
    private Op op1;
    private Op op2;
    
    public CPUTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        program = new ArrayList<>();
        op0 = new Op();
        program.add(op0);
        op1 = new Op();
        program.add(op1);
        op2 = new Op();
        program.add(op2);
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testRunSimple() {
        CPU<Op> target = new CPU<>(program);
        
        target.run();
        
        assertEquals(1, op0.execCt, "op 0 not executed correctly");
        assertEquals(1, op1.execCt, "op 1 not executed correctly");
        assertEquals(1, op2.execCt, "op 2 not executed correctly");
    }

    @Test
    public void testRunPreStop() {
        CPU<Op> target = new CPU<>(program, this::stopMethod, null);
        
        target.run();
        
        assertEquals(1, op0.execCt, "op 0 not executed correctly");
        assertEquals(0, op1.execCt, "op 1 not executed correctly");
        assertEquals(0, op2.execCt, "op 2 not executed correctly");
    }

    @Test
    public void testRunPostStop() {
        CPU<Op> target = new CPU<>(program, null, this::stopMethod);
        
        target.run();
        
        // Note that the post-exec call occurs after the pc was incremented
        assertEquals(1, op0.execCt, "op 0 not executed correctly");
        assertEquals(0, op1.execCt, "op 1 not executed correctly");
        assertEquals(0, op2.execCt, "op 2 not executed correctly");
    }
    
    private boolean stopMethod(CPU cpu) {
        return cpu.getPc() != 1;
    }
}
