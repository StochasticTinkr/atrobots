package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public abstract class ConditionalJump extends Instruction {
    public ConditionalJump(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    final protected void perform(Computer computer) {
        if (conditionMet(computer.getFlags())) {
            computer.jump();
        }
    }

    protected abstract boolean conditionMet(Flags flags);
}
