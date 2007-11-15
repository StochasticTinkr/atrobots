package net.virtualinfinity.atrobots.computer;

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
