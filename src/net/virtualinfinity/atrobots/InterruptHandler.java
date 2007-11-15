package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Computer;

/**
 * @author Daniel Pitts
 */
public abstract class InterruptHandler {
    private int cost;
    private Computer computer;

    protected abstract void handleInterrupt();

    public final InterruptHandler costs(int cost) {
        this.cost = cost;
        return this;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public final void call() {
        computer.consumeCycles(cost);
        handleInterrupt();
    }
}
