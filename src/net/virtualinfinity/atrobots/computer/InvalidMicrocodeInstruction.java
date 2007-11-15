package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class InvalidMicrocodeInstruction extends Instruction {
    public InvalidMicrocodeInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.invalidMicrocodeError();
    }
}
