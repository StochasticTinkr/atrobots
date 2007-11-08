package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public abstract class Instruction {
    private final int baseExecutionCost;

    protected Instruction(int baseExecutionCost) {
        this.baseExecutionCost = baseExecutionCost;
    }

    public final void execute(Computer computer) {
        computer.consumeCycles(baseExecutionCost);
    }

    protected abstract void perform(Computer computer);
}
