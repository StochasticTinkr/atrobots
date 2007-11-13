package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public abstract class DamageFunction {
    public abstract double getDamageAmount(ArenaObject object);

    public void inflictDamage(Robot cause, Robot robot) {
        final double amount = getDamageAmount(robot);
        if (amount > 0) {
            robot.inflictDamage(cause, amount);
        }
    }
}
