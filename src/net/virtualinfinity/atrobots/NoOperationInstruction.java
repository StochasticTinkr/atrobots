package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class NoOperationInstruction extends Instruction {
    public NoOperationInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
    }
}
