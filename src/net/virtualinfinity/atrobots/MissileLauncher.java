package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.RelativeAngle;

/**
 * @author Daniel Pitts
 */
public class MissileLauncher {
    private Heading heading;
    private double power;
    private Position position;
    private Robot robot;

    public MissileLauncher(Robot robot, Position position, Heading heading, double power) {
        this.robot = robot;
        this.position = position;
        this.heading = heading;
        this.power = power;
    }

    public MissileLauncher() {
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public PortHandler getActuator() {
        return new PortHandler() {
            public void write(short value) {
                getComputer().consumeCycles(3);
                fireMissile(RelativeAngle.fromBygrees(value));
            }
        };
    }

    private void fireMissile(RelativeAngle shift) {
        final byte value = shift.getSignedBygrees();
        int bygrees = Math.max(-4, Math.min(value, 4));
        AbsoluteAngle angle = heading.getAngle().counterClockwise(RelativeAngle.fromBygrees(bygrees));
        final Missile missile = new Missile(robot, position, angle, getPower());
        getArena().fireMissile(missile);
        robot.getHeat().warm(Temperature.fromLogScale(robot.isOverburn() ? 20 : 30));
    }

    private double getPower() {
        return robot.isOverburn() ? power * 1.30 : power;
    }

    public Arena getArena() {
        return robot.getArena();
    }
}
