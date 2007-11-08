package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class SubtractInstruction extends AbstractCombiningInstruction {
    public SubtractInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return first - second;
    }
}
