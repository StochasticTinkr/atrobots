package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Missile extends ArenaObject {
    private final Robot robot;

    public Missile(Robot robot, Position position, Angle angle) {
        this.robot = robot;
        this.position.copyFrom(position);
        this.heading.setAngle(angle);
    }
}
