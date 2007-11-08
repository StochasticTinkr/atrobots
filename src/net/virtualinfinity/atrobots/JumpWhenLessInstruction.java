package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class JumpWhenLessInstruction extends ConditionalJump {
    protected JumpWhenLessInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected boolean conditionMet(Flags flags) {
        return flags.isLess();
    }
}
