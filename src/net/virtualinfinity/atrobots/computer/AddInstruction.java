package net.virtualinfinity.atrobots.computer;

/**
 * @author Daniel Pitts
 */
public class AddInstruction extends AbstractCombiningInstruction {
    public AddInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return first + second;
    }
}
