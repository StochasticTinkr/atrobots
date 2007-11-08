package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class BitwiseExclusiveOrInstruction extends AbstractCombiningInstruction {
    public BitwiseExclusiveOrInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return first ^ second;
    }
}
