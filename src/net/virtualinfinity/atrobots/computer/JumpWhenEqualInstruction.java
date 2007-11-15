package net.virtualinfinity.atrobots.computer;

import net.virtualinfinity.atrobots.Flags;

/**
 * @author Daniel Pitts
 */
public class JumpWhenEqualInstruction extends ConditionalJump {
    protected JumpWhenEqualInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected boolean conditionMet(Flags flags) {
        return flags.isEqual();
    }
}