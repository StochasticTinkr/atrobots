package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Computer;

/**
 * @author Daniel Pitts
 */
public class ResetInterrupt extends InterruptHandler {
    private final Computer computer;

    public ResetInterrupt(Computer computer) {
        this.computer = computer;
    }

    public void handleInterrupt() {
        computer.reset();
    }
}
