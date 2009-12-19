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
            if (collisionPoint.minus(robot.getPosition().getVector()).getMagnitude().getMeters() < 8) {
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
        final Distance x = position.getX().negate();
        final Distance y = position.getY().negate();
        final Vector vector = heading.getAngle().toVector(Distance.unit());
        if (position.getX().getMeters() <= 0) {
            return vector.times(x.dividedBy(vector.getX()));
        }
        if (position.getX().getMeters() >= 1000) {
            return vector.times(Distance.fromMeters(1000).plus(x).dividedBy(vector.getX()));
        }
        if (position.getY().getMeters() <= 0) {
            return vector.times(y.dividedBy(vector.getY()));
        }
        if (position.getY().getMeters() >= 1000) {
            return vector.times(Distance.fromMeters(1000).plus(y).dividedBy(vector.getY()));
        }
        throw new IllegalStateException("Should be outside arena.");

    }

    private boolean isOutsideArena() {
        return position.getX().getMeters() < 0 || position.getX().getMeters() > 1000
                || position.getY().getMeters() < 0 || position.getY().getMeters() > 1000;
    }

}
