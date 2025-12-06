package bob.data;

import bob.util.Assert;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.Getter;
import lombok.Setter;

/**
 * A processor for various puzzles.
 * <p>
 * Puzzle solutions that utilize this class will likely have an "{@code Instr}" class that contains:
 * </p>
 * <ul>
 * <li>An enum "{@code OpCode}" defining the valid operation codes</li>
 * <li>The "microcode" to implement the operation codes</li>
 * </ul>
 * <p>
 * Puzzles that simply require the program to be executed before checking the results can use the {@link #run() run}
 * method to execute the program. If the puzzle requires checking state during execution, it needs to call the
 * {@link #step() step} method to execute each operation.
 * </p>
 *
 * @param <O> The class of operations to execute
 */
@Getter
public class CPU<O extends CPU.Operation> {

    public static interface Operation {

        /**
         * This method should contain the microcode to implement all operations.
         * @param cpu The CPU that the operation will run on
         */
        void execute(CPU cpu);
    }

    /**
     * The current program counter, identifies the next instruction to execute.
     */
    @Setter
    private int pc;

    /**
     * The registers.
     */
    private final long[] reg = new long[16];
    
    /**
     * The stack.
     */
    private final Deque<Long> stack = new ArrayDeque<>();

    /**
     * The memory. Note that the size of the address space is usually either unknown or potentially very large. Not much
     * memory is actually used, so it seems reasonable to store the memory contents as a map of address to value. All
     * addresses not in the map are assumed to be at some pre-defined value, probably 0.
     */
    private final Map<Long, Long> mem = new HashMap<>();

    /**
     * The list of operations to execute.
     */
    private List<O> program;

    /**
     * A method to call before executing the current instruction. If this method returns {@code false}, the current
     * instruction is not executed and the {@link #step() step} method returns {@code false}.
     */
    private Predicate<CPU> prestep;

    /**
     * A method to call after executing the current instruction. If this method returns {@code false}, the
     * {@link #step() step} method returns {@code false}.
     */
    private Predicate<CPU> poststep;

    /**
     * Create a CPU to execute the specified program.
     *
     * @param program The program to execute
     */
    public CPU(List<O> program) {
        this(program, null, null);
    }

    /**
     * Create a CPU to execute the specified program using the specified pre- and post-step methods.
     * @param program The program to execute
     * @param prestep The pre-step method to call, {@code null} to skip
     * @param poststep The post-step method to call, {@code null} to skip
     */
    public CPU(List<O> program, Predicate<CPU> prestep, Predicate<CPU> poststep) {
        this.program = program;
        this.prestep = (prestep == null) ? this::defaultPrePostStep : prestep;
        this.poststep = (poststep == null) ? this::defaultPrePostStep : poststep;
    }

    /**
     * Execute the next operation.
     * @return {@code true} if execution should continue
     */
    public boolean step() {
        if (!prestep.test(this)) {
            return false;
        }
        
        int oldpc = pc;
        O instr = program.get(pc);
        instr.execute(this);
        Assert.that((pc != oldpc), "pc not changed by microcode for " + instr);
        
        return poststep.test(this);
    }

    /**
     * Execute the program until the {@code pc} is not valid or {@link #step() step} returns {@code false}.
     */
    public void run() {
        boolean cont = true;
        while ((pc >= 0) && (pc < program.size()) && cont) {
            cont = step();
        }
    }
    
    /**
     * Retrieve the top of stack without removing it.
     * @return The value at the top of the stack or {@code null} if the stack is empty
     */
    public Long stackPeek() {
        return stack.peekFirst();
    }
    
    /**
     * Remove the top entry in the stack and return it.
     * @return The value at the top of the stack or {@code null} if the stack is empty
     */
    public Long stackPop() {
        return stack.pollFirst();
    }
    
    /**
     * Push a value onto the stack
     * @param newval The value to add to the stack
     */
    public void stackPush(long newval) {
        stack.addFirst(newval);
    }
    
    /**
     * Read the value in the specified memory address.
     * @param addr The address to read
     * @return The value or {@code null} if the address has not yet been written
     */
    public Long memRead(long addr) {
        return mem.get(addr);
    }
    
    /**
     * Write the value to the specified memory address.
     * @param addr The address to write
     * @param value The value to write
     */
    public void memWrite(long addr, long value) {
        mem.put(addr, value);
    }
    
    /*
     * Default method for both prestep and poststep
     */
    private boolean defaultPrePostStep(CPU cpu) {
        return true;
    }
}
