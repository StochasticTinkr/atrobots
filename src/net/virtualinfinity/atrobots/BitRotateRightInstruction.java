package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class BitRotateRightInstruction extends AbstractCombiningInstruction {
    public BitRotateRightInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected int combine(short first, short second) {
        return (first >>> second) | (first << (16 - second));
    }
}