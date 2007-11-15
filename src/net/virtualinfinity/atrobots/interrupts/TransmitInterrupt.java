package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.Transceiver;
import net.virtualinfinity.atrobots.computer.MemoryCell;

/**
 * @author Daniel Pitts
 */
public class TransmitInterrupt extends InterruptHandler {
    private final Transceiver transceiver;
    private final MemoryCell source;

    public TransmitInterrupt(Transceiver transceiver, MemoryCell source) {
        this.transceiver = transceiver;
        this.source = source;
    }

    public void handleInterrupt() {
        transceiver.send(source.signed());

    }
}
