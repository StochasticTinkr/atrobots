package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public abstract class AbstractCombiningInstruction extends Instruction {
    public AbstractCombiningInstruction(int baseExecutionCost) {
        super(baseExecutionCost);
    }

    protected final void perform(Computer computer) {
        computer.setOperandValue(1, (short)combine(computer.getOperandValue(1), computer.getOperandValue(2)));
    }

    protected abstract int combine(short first, short second);
}
