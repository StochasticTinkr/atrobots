package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class LoopInstruction extends Instruction {
    public LoopInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        final MemoryCell cx = computer.getRegisters().getCx();
        cx.decrement();
        if (cx.signed() > 0) {
            computer.jump();
        }
    }
}
