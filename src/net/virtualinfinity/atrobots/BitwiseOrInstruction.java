package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class BitwiseOrInstruction extends AbstractCombiningInstruction {
    public BitwiseOrInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return first | second;
    }
}
