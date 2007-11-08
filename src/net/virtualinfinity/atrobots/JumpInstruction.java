package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class JumpInstruction extends Instruction {
    protected JumpInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected void perform(Computer computer) {
        computer.jump();
    }
}
