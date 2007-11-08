package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class PushInstruction extends Instruction {
    public PushInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.push();
    }
}
