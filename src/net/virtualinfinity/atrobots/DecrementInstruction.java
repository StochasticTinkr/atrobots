package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class DecrementInstruction extends Instruction {
    public DecrementInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.decrementOperand(1);
    }
}