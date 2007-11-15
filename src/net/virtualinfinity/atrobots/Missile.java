package net.virtualinfinity.atrobots;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * @author Daniel Pitts
 */
public class Missile extends ArenaObject {
    private final Robot robot;

    public Missile(Robot robot, Position position, AbsoluteAngle angle) {
        this.robot = robot;
        this.position.copyFrom(position);
        this.heading.setAngle(angle);
    }

    protected ArenaObjectSnapshot createSpecificSnapshot() {
        return new MissileSnapshot();
    }

    public Speed getSpeed() {
        return speed;
    }


    @Override
    public void checkCollision(Robot robot) {
        if (!isDead()) {
            //TODO: Better robot collision detection.
            if (robot.getPosition().getVectorTo(position).getMagnatude().getMeters() < 14) {
                explode();
            }
        }
    }

    private void explode() {
        getArena().explosion(this.robot, new LinearDamageFunction(position, 1, 14));
        setDead(true);
    }

    public void update(Duration duration) {
        super.update(duration);    //To change body of overridden methods use File | Settings | File Templates.
        if (!isDead() && isOutsideArena()) {
            explode();
        }
    }

    private boolean isOutsideArena() {
        return position.getX().getMeters() < 0 || position.getX().getMeters() > 1000
                || position.getY().getMeters() < 0 || position.getY().getMeters() > 1000;
    }

    private static class MissileSnapshot extends ArenaObjectSnapshot {
        public void paint(Graphics2D g2d) {
            g2d.setPaint(Color.white);
            g2d.draw(new Line2D.Double(getX(), getY(), getX() + getVelocityX(), getY() + getVelocityY()));
        }
    }
}
