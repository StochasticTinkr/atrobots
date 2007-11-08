package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class CallInterruptInstruction extends Instruction {
    public CallInterruptInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.callInterrupt();
    }
}
