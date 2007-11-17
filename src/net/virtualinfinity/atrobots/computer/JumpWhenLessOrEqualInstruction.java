package net.virtualinfinity.atrobots.computer;

import net.virtualinfinity.atrobots.Flags;

/**
 * @author Daniel Pitts
 */
public class JumpWhenLessOrEqualInstruction extends ConditionalJump {
    protected JumpWhenLessOrEqualInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected boolean conditionMet(Flags flags) {
        return flags.isLess() || flags.isEqual();
    }
}