package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class BitShiftRightInstruction extends AbstractCombiningInstruction {
    public BitShiftRightInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return (first & 0xFFFF) >>> second;
    }
}