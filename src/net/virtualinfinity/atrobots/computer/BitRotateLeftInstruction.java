package net.virtualinfinity.atrobots.computer;

import net.virtualinfinity.atrobots.AbstractCombiningInstruction;

/**
 * @author Daniel Pitts
 */
public class BitRotateLeftInstruction extends AbstractCombiningInstruction {
    public BitRotateLeftInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return (first << second) | ((first & 0xFFFF) >>> (16 - second));
    }
}