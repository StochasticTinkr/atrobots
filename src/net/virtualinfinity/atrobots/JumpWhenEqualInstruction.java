package net.virtualinfinity.atrobots;

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