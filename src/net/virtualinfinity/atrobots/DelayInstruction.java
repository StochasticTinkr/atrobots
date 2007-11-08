package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class DelayInstruction extends Instruction {
    public DelayInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.consumeCycles(computer.getOperandValue(1));
    }
}
