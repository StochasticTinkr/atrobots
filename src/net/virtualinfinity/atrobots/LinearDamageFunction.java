package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class LinearDamageFunction extends DamageFunction {
    private final double multiplier;
    private final Position position;
    private final double damageAtCenter;

    public LinearDamageFunction(Position position, double multiplier, double damagePerMeter) {
        this.position = position;
        this.multiplier = multiplier;
        this.damageAtCenter = damagePerMeter;
    }

    public double getDamageAmount(ArenaObject object) {
        final Distance distance = object.getPosition().getVectorTo(position).getMagnatude();
        return Math.min(0, damageAtCenter - distance.getMeters() * multiplier);
    }

}
