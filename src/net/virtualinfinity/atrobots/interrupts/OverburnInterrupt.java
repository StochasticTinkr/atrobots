package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.MemoryCell;
import net.virtualinfinity.atrobots.Robot;

/**
 * @author Daniel Pitts
 */
public class OverburnInterrupt extends InterruptHandler {
    private final Robot robot;
    private final MemoryCell toggler;

    public OverburnInterrupt(Robot robot, MemoryCell toggler) {
        this.robot = robot;
        this.toggler = toggler;
    }

    public void handleInterrupt() {
        robot.setOverburn(toggler.signed() != 0);
    }
}
