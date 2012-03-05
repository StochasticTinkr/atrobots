package net.virtualinfinity.atrobots.hardware;

import net.virtualinfinity.atrobots.computer.Resettable;
import net.virtualinfinity.atrobots.computer.ShutdownListener;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.measures.Temperature;
import net.virtualinfinity.atrobots.ports.PortHandler;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;

/**
 * @author Daniel Pitts
 */
public class Shield implements Resettable, ShutdownListener {
    private boolean active;
    private Robot robot;
    private double heatFraction = 0;
    private double damageFraction = 1;

    public Shield(double strength) {
        this.heatFraction = strength;
        this.damageFraction = strength;
    }

    public PortHandler getLatch() {
        return new PortHandler() {
            public short read() {
                return (short) (isActive() ? 1 : 0);
            }

            public void write(short value) {
                setActive(value != 0);
            }
        };
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        robot.getHeatSinks().blockHeat(active);
    }

    public void reset() {
        setActive(false);
    }

    public double absorbDamage(double damageAmount) {
        if (active) {
            robot.getHeatSinks().warm(Temperature.fromLogScale((int) Math.round(damageAmount * heatFraction)));
            return damageAmount * damageFraction;
        }
        return damageAmount;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public void update(Duration duration) {
        if (active && heatFraction > 0) {
            robot.getHeatSinks().warm(Temperature.fromLogScale(duration.getCycles() / 3.0));
        }
    }

    public void shutDown() {
        setActive(false);
    }
}
