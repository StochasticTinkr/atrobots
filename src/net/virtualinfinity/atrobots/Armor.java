package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Armor {
    private double percentRemaining;

    public Armor() {
    }

    public PortHandler getSensor() {
        return new PortHandler() {
            public short read() {
                return (short) Math.round(percentRemaining());
            }
        };
    }

    private double percentRemaining() {
        return percentRemaining;
    }

    public void setRemaining(double remaining) {
        percentRemaining = 0;
    }
}
