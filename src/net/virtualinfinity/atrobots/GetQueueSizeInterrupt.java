package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class GetQueueSizeInterrupt extends InterruptHandler {
    private final CommunicationsQueue commQueue;
    private final MemoryCell destination;

    public GetQueueSizeInterrupt(CommunicationsQueue commQueue, MemoryCell destination) {
        this.commQueue = commQueue;
        this.destination = destination;
    }

    public void handleInterrupt() {
        destination.set(commQueue.size());
    }
}
