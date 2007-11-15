package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class SwapInstruction extends Instruction {
    public SwapInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.getRegisters().getSwap().set(computer.getOperandValue(1));
        computer.setOperandValue(1, computer.getOperandValue(2));
        computer.setOperandValue(2, computer.getRegisters().getSwap().signed());
    }
}