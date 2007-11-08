package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class PopInstruction extends Instruction {
    public PopInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
             computer.pop();
    }
}