package net.virtualinfinity.atrobots.simulation.missile;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.measures.Vector;
import net.virtualinfinity.atrobots.simulation.arena.ArenaObject;
import net.virtualinfinity.atrobots.simulation.arena.LinearDamageFunction;
import net.virtualinfinity.atrobots.simulation.arena.Position;
import net.virtualinfinity.atrobots.simulation.arena.Speed;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;
import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.MissileSnapshot;

/**
 * An explosive projectile arena object.
 *
 * @author Daniel Pitts
 */
public class Missile extends ArenaObject {
    private final Robot robot;
    private final double power;
    private final boolean overburn;

    public Missile(Robot robot, Position position, AbsoluteAngle angle, double power) {
        this.robot = robot;
        this.power = power;
        this.position.copyFrom(position);
        this.heading.setAngle(angle);
        this.overburn = robot.isOverburn();
        getSpeed().setDistanceOverTime((32) * (power), Duration.ONE_CYCLE);
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        return new MissileSnapshot(overburn);
    }

    /**
     * Get the speed of this missile.
     *
     * @return the speed
     */
    public Speed getSpeed() {
        return speed;
    }


    /**
     * See if this robot collides with this missile.
     *
     * @param robot the robot to check collision against.
     */
    public void checkCollision(Robot robot) {
        if (robot == this.robot || isDead()) {
            return;
        }
        final Vector collisionPoint = getCollisionPoint(robot);
        if (collisionPoint != null) {
            if (collisionPoint.minus(robot.getPosition().getVector()).getMagnitudeSquared() < 196) {
                position.copyFrom(new Position(collisionPoint));
                explode();
            }
        }
    }

    private Vector getCollisionPoint(Robot robot) {
        return robot.getPosition().getVector().perpendicularIntersectionFrom(getPosition().getVector(), heading.getAngle(), speed.times(Duration.ONE_CYCLE));
    }


    private void explode() {
        getArena().explosion(this.robot, new LinearDamageFunction(position, power, 14));
        die();
    }

    @Override
    public void update(Duration duration) {
        super.update(duration);
        if (!isDead()) {
            checkWallCollision();
        }
    }

    private void checkWallCollision() {
        if (isOutsideArena()) {
            position.move(getWallIntersectionDelta());
            explode();
        }
    }

    private Vector getWallIntersectionDelta() {
        final double x = -position.getX();
        final double y = -position.getY();
        final Vector vector = heading.getAngle().toUnitVector();
        if (position.getX() <= 0) {
            return vector.times(x / (vector.getX()));
        }
        if (position.getX() >= 1000) {
            return vector.times((1000 + x) / (vector.getX()));
        }
        if (position.getY() <= 0) {
            return vector.times(y / (vector.getY()));
        }
        if (position.getY() >= 1000) {
            return vector.times((1000 + y) / (vector.getY()));
        }
        throw new IllegalStateException("Should be outside arena.");

    }

    private boolean isOutsideArena() {
        return position.getX() < 0 || position.getX() > 1000
                || position.getY() < 0 || position.getY() > 1000;
    }

}
