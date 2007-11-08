package net.virtualinfinity.atrobots;

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