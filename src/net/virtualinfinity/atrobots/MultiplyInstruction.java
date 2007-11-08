package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class MultiplyInstruction extends AbstractCombiningInstruction {
    public MultiplyInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return first * second;
    }
}
