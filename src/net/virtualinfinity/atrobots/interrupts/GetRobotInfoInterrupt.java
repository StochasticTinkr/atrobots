package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.Robot;
import net.virtualinfinity.atrobots.computer.MemoryCell;
import net.virtualinfinity.atrobots.measures.Duration;

/**
 * @author Daniel Pitts
 */
public class GetRobotInfoInterrupt extends InterruptHandler {
    private final Robot robot;
    private final MemoryCell speed;
    private final MemoryCell lastDamageTaken;
    private final MemoryCell lastDamageGiven;

    public GetRobotInfoInterrupt(Robot robot, MemoryCell speed, MemoryCell lastDamageTaken, MemoryCell lastDamageGiven) {
        this.robot = robot;
        this.speed = speed;
        this.lastDamageTaken = lastDamageTaken;
        this.lastDamageGiven = lastDamageGiven;
    }

    public void handleInterrupt() {
        speed.set((short) Math.round(robot.getSpeed().times(Duration.ONE_CYCLE).getCentimeters()));
        lastDamageGiven.set((short) robot.getLastDamageGiven().getCycles());
        lastDamageTaken.set((short) robot.getLastDamageTaken().getCycles());
    }
}
