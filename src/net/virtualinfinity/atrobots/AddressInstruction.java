package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class AddressInstruction extends Instruction {
    public AddressInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.setOperandValue(1, (short) computer.getOperandAddress(2));
    }
}