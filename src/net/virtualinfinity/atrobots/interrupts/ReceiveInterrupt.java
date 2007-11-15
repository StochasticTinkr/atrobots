package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.CommunicationsQueue;
import net.virtualinfinity.atrobots.MemoryCell;

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
