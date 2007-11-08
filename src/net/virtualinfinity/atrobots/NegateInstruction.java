package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class NegateInstruction extends Instruction {
    public NegateInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.setOperandValue(1, (short) -computer.getOperandValue(1));
    }
}
