package net.virtualinfinity.atrobots.simulation.arena;


import net.virtualinfinity.atrobots.measures.Vector;
import net.virtualinfinity.atrobots.simulation.atrobot.DamageInflicter;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;

/**
 * @author Daniel Pitts
 */
public abstract class ExplosionFunction {
    public void inflictDamage(DamageInflicter cause, Robot robot) {
        final double amount = getDamageAmount(robot);
        if (amount > 0) {
            robot.inflictDamage(cause, amount);
        }
    }

    public abstract double getDamageAmount(ArenaObject object);

    public abstract Vector getCenter();

    public abstract double getRadius();
}
