package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class SetCXInstruction extends Instruction {
    public SetCXInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.getRegisters().getCx().set(computer.getOperandValue(1));
    }
}
