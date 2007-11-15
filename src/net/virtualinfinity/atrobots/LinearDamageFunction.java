package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.Vector;

/**
 * @author Daniel Pitts
 */
public class LinearDamageFunction extends ExplosionFunction {
    private final double multiplier;
    private final Position position;
    private final double damageAtCenter;

    public LinearDamageFunction(Position position, double multiplier, double damageAtCenter) {
        this.position = position;
        this.multiplier = multiplier;
        this.damageAtCenter = damageAtCenter;
    }

    public double getDamageAmount(ArenaObject object) {
        final Distance distance = object.getPosition().getVectorTo(position).getMagnatude();
        return Math.max(0, (damageAtCenter - distance.getMeters()) * multiplier);
    }

    public Vector getCenter() {
        return position.getVector();
    }

    public Distance getRadius() {
        return Distance.fromMeters(damageAtCenter);
    }

}
