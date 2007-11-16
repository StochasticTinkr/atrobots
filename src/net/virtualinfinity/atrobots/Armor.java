package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Armor {
    private double percentRemaining = 100;
    private Robot robot;

    public Armor() {
    }

    public Armor(double percentRemaining) {
        this.percentRemaining = percentRemaining;
    }

    public PortHandler getSensor() {
        return new PortHandler() {
            public short read() {
                return (short) Math.round(percentRemaining());
            }
        };
    }

    private double percentRemaining() {
        return getRemaining();
    }

    public void setRemaining(double percentRemaining) {
        this.percentRemaining = percentRemaining;
        checkDead();
    }

    private void checkDead() {
        if (percentRemaining <= 0) {
            robot.explode();
        }
    }

    public double getRemaining() {
        return percentRemaining;
    }

    public void inflictDamage(double damageAmount) {
        percentRemaining -= damageAmount;
        checkDead();
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
