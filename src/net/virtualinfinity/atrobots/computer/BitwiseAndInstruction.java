package net.virtualinfinity.atrobots.computer;

import net.virtualinfinity.atrobots.AbstractCombiningInstruction;

/**
 * @author Daniel Pitts
 */
public class BitwiseAndInstruction extends AbstractCombiningInstruction {
    public BitwiseAndInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return first & second;
    }
}
