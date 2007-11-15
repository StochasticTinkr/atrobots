package net.virtualinfinity.atrobots.computer;

import net.virtualinfinity.atrobots.AbstractCombiningInstruction;

/**
 * @author Daniel Pitts
 */
public class BitShiftLeftInstruction extends AbstractCombiningInstruction {
    public BitShiftLeftInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return first << second;
    }
}