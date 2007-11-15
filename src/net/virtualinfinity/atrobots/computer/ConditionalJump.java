package net.virtualinfinity.atrobots.computer;

import net.virtualinfinity.atrobots.Flags;

/**
 * @author Daniel Pitts
 */
public abstract class ConditionalJump extends Instruction {
    public ConditionalJump(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    final protected void perform(Computer computer) {
        if (conditionMet(computer.getFlags())) {
//            System.out.println("Doing jump");
            computer.jump();
        } else {
//            System.out.println("Not jumping");
        }
    }

    protected abstract boolean conditionMet(Flags flags);
}
