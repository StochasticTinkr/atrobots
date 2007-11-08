package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class JumpWhenGreaterOrEqualInstruction extends ConditionalJump {
    protected JumpWhenGreaterOrEqualInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected boolean conditionMet(Flags flags) {
        return flags.isGreater() || flags.isEqual();
    }
}