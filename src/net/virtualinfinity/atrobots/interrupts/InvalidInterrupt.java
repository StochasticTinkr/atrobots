package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.computer.ComputerErrorHandler;

/**
 * @author Daniel Pitts
 */
public class InvalidInterrupt extends InterruptHandler {
    private final ComputerErrorHandler computerErrorHandler;

    public InvalidInterrupt(ComputerErrorHandler computer) {
        this.computerErrorHandler = computer;
    }

    public void handleInterrupt() {
        computerErrorHandler.invalidInterruptError();
    }
}
