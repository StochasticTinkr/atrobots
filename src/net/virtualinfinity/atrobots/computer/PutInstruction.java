package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class PutInstruction extends Instruction {
    public PutInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.getMemory().set(computer.getOperandValue(2), computer.getOperandValue(1));
    }
}