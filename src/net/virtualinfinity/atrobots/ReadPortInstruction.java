package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class ReadPortInstruction extends Instruction {
    public ReadPortInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.readPort();
    }
}
