package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public abstract class ExplosionFunction {
    public void inflictDamage(Robot cause, Robot robot) {
        final double amount = getDamageAmount(robot);
        if (amount > 0) {
            robot.inflictDamage(cause, amount);
        }
    }

    public abstract double getDamageAmount(ArenaObject object);

    public abstract Vector getCenter();

    public abstract Distance getRadius();
}
