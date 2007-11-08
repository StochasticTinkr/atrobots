package net.virtualinfinity.atrobots;

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
