package net.virtualinfinity.atrobots.computer;


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