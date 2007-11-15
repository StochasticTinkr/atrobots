package net.virtualinfinity.atrobots.computer;


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
