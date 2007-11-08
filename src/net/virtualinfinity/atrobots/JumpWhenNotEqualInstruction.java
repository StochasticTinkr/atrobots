package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class JumpWhenNotEqualInstruction extends ConditionalJump {
    protected JumpWhenNotEqualInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected boolean conditionMet(Flags flags) {
        return !flags.isEqual();
    }
}