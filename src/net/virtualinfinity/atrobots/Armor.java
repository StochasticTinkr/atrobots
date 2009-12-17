package net.virtualinfinity.atrobots;

/**
 * Represents the armor of a robot.
 *
 * @author Daniel Pitts
 */
public class Armor {
    private double pointsRemaining;
    private Robot robot;

    /**
     * Construct armor with the specific number of points remaining.
     *
     * @param points the number points this armor starts with.
     */
    public Armor(double points) {
        this.pointsRemaining = points;
    }

    /**
     * Get a port handler which reads the number of remaining armor points.
     *
     * @return the Armor sensor PortHandler.
     */
    public PortHandler getSensor() {
        return new PortHandler() {
            public short read() {
                return (short) Math.round(getRemaining());
            }
        };
    }

    /**
     * Destroy this robot by inflicting the damage equal to the current remaining points.
     */
    public void destruct() {
        inflictDamage(getRemaining());
    }

    private void checkDead() {
        if (pointsRemaining <= 0) {
            robot.explode();
        }
    }

    /**
     * Get the remaining armor.
     *
     * @return the number of points remaining.
     */
    public double getRemaining() {
        return pointsRemaining;
    }

    /**
     * Inflict the specific amount of damage. Causes the robot to explode if the points drop to 0 or below.
     *
     * @param damageAmount the amount of damage to inflict.
     */
    public void inflictDamage(double damageAmount) {
        pointsRemaining -= damageAmount;
        checkDead();
    }

    /**
     * Get the robot which this armor protects.
     *
     * @return the robot.
     */
    public Robot getRobot() {
        return robot;
    }


    /**
     * Set the armor which this robot protects.
     *
     * @param robot the robot.
     */
    void setRobot(Robot robot) {
        this.robot = robot;
    }
}
