package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class ErrorInstruction extends Instruction {
    public ErrorInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.genericError(computer.getOperandValue(1));
    }
}
