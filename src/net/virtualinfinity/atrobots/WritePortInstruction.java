package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class WritePortInstruction extends Instruction {
    public WritePortInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.writePort();
    }
}
