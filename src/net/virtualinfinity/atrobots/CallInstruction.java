package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class CallInstruction extends Instruction {
    public CallInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.call();
    }
}
