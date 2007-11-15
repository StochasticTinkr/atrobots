package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.Vector;

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
