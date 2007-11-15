package net.virtualinfinity.atrobots.computer;


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
