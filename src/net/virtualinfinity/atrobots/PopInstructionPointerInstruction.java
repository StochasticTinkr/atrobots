package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class PopInstructionPointerInstruction extends Instruction {
    public PopInstructionPointerInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.popInstructionPointer();
    }
}
