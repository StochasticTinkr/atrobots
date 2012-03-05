package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.computer.InterruptHandler;
import net.virtualinfinity.atrobots.computer.MemoryCell;
import net.virtualinfinity.atrobots.simulation.arena.Odometer;


/**
 * @author Daniel Pitts
 */
public class ResetMetersInterrupt extends InterruptHandler {
    private final MemoryCell meters;
    private final Odometer odometer;

    public ResetMetersInterrupt(MemoryCell meters, Odometer odometer) {
        this.odometer = odometer;
        this.meters = meters;
    }

    public void handleInterrupt() {
        odometer.setDistance((0));
        meters.set((short) 0);
    }
}
