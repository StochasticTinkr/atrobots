package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.measures.RelativeAngle;

/**
 * @author Daniel Pitts
 */
public class MissileLauncher {
    private final Heading heading;
    private final double power;
    private final Position position;
    private final Robot robot;

    public MissileLauncher(Robot robot, Position position, Heading heading, double power) {
        this.robot = robot;
        this.position = position;
        this.heading = heading;
        this.power = power;
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
        missile.setOverburn(robot.isOverburn());
        missile.getSpeed().setDistanceOverTime(Distance.fromMeters(32).times(getPower()), Duration.ONE_CYCLE);
        getArena().fireMissile(missile);
        robot.getHeat().warm(Temperature.fromLogScale(robot.isOverburn() ? 20 : 30));
    }

    private double getPower() {
        return robot.isOverburn() ? power * 1.30 : power;
    }

    public Arena getArena() {
        return robot.getEntrant().getGame().getRound().getArena();
    }
}
