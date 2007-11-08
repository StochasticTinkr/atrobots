package net.virtualinfinity.atrobots;

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
