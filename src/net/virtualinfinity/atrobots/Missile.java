package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.measures.Vector;
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
        getSpeed().setDistanceOverTime(Distance.fromMeters(32).times(power), Duration.ONE_CYCLE);
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
    @Override
    public void checkCollision(Robot robot) {
        if (robot == this.robot || isDead()) {
            return;
        }
        final Vector collisionPoint = getCollisionPoint(robot);
        if (collisionPoint != null) {
            if (collisionPoint.minus(robot.getPosition().getVector()).getMagnatude().getMeters() < 8) {
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
        final Distance x = Distance.fromMeters(0).minus(position.getX());
        final Distance y = Distance.fromMeters(0).minus(position.getY());
        if (position.getX().getMeters() <= 0) {
            return heading.getAngle().projectAngle(Vector.createCartesian(x, Distance.fromMeters(0)));
        }
        if (position.getX().getMeters() >= 1000) {
            return heading.getAngle().projectAngle(Vector.createCartesian(Distance.fromMeters(1000).plus(x), Distance.fromMeters(0)));
        }
        if (position.getY().getMeters() <= 0) {
            return heading.getAngle().projectAngle(Vector.createCartesian(Distance.fromMeters(0), y));
        }
        if (position.getY().getMeters() >= 1000) {
            return heading.getAngle().projectAngle(Vector.createCartesian(Distance.fromMeters(0), Distance.fromMeters(1000).plus(y)));
        }
        throw new AssertionError("Should be outside arena.");

    }

    private boolean isOutsideArena() {
        return position.getX().getMeters() < 0 || position.getX().getMeters() > 1000
                || position.getY().getMeters() < 0 || position.getY().getMeters() > 1000;
    }

}
