package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class GetInstruction extends Instruction {
    public GetInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.setOperandValue(1, computer.getMemory().get((short) computer.getOperandAddress(2)));
    }
}