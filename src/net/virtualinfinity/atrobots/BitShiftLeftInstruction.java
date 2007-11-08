package net.virtualinfinity.atrobots;

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