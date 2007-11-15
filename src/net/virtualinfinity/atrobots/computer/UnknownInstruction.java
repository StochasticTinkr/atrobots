package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class UnknownInstruction extends Instruction {
    public UnknownInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.unknownInstructionError();
    }
}
