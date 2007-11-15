package net.virtualinfinity.atrobots.computer;

import net.virtualinfinity.atrobots.Flags;

/**
 * @author Daniel Pitts
 */
public class JumpWhenZeroInstruction extends ConditionalJump {
    protected JumpWhenZeroInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected boolean conditionMet(Flags flags) {
        return flags.isZero();
    }
}