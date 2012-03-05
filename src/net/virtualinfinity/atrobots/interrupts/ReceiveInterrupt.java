package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.computer.CommunicationsQueue;
import net.virtualinfinity.atrobots.computer.InterruptHandler;
import net.virtualinfinity.atrobots.computer.MemoryCell;

/**
 * @author Daniel Pitts
 */
public class ReceiveInterrupt extends InterruptHandler {
    private final CommunicationsQueue queue;
    private final MemoryCell destination;

    public ReceiveInterrupt(CommunicationsQueue queue, MemoryCell destination) {
        this.queue = queue;
        this.destination = destination;
    }

    public void handleInterrupt() {
        queue.popTo(destination);
    }
}
