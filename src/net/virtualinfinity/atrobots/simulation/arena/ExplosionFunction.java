package net.virtualinfinity.atrobots.simulation.arena;


import net.virtualinfinity.atrobots.measures.Vector;
import net.virtualinfinity.atrobots.simulation.atrobot.DamageInflicter;

/**
 * @author Daniel Pitts
 */
public abstract class ExplosionFunction {
    public void inflictDamage(DamageInflicter cause, TangibleArenaObject robot) {
        final double amount = getDamageAmount(robot);
        if (amount > 0) {
            robot.inflictDamage(cause, amount);
        }
    }

    public abstract double getDamageAmount(ArenaObject object);

    public abstract Vector getCenter();

    public abstract double getRadius();
}
