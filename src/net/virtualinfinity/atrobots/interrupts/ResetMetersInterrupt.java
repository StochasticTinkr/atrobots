package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.Robot;
import net.virtualinfinity.atrobots.computer.MemoryCell;


/**
 * @author Daniel Pitts
 */
public class ResetMetersInterrupt extends InterruptHandler {
    private final Robot robot;
    private final MemoryCell meters;

    public ResetMetersInterrupt(Robot robot, MemoryCell meters) {
        this.robot = robot;
        this.meters = meters;
    }

    public void handleInterrupt() {
        robot.getOdometer().setDistance((0));
        meters.set((short) 0);
    }
}
