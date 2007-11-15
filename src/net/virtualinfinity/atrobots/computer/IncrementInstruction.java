package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class IncrementInstruction extends Instruction {
    public IncrementInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.incrementOperand(1);
    }
}
