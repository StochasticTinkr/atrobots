package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.simulation.atrobot.Robot;

/**
 * @author Daniel Pitts
 */
public class DestructInterrupt extends InterruptHandler {
    private final Robot robot;

    public DestructInterrupt(Robot robot) {
        this.robot = robot;
    }

    public void handleInterrupt() {
        robot.destruct();
    }
}
