package net.virtualinfinity.atrobots.simulation.atrobot;


import net.virtualinfinity.atrobots.computer.ShutdownListener;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.ports.PortHandler;
import net.virtualinfinity.atrobots.simulation.arena.Speed;

/**
 * @author Daniel Pitts
 */
public class Throttle implements ShutdownListener {
    int desiredPower;
    int power;
    Speed speed;
    private static final int MAX_ACCELERATION = 4;
    private static final double STANDARD_MAX_VELOCITY = 4.0;
    private double powerRatio = STANDARD_MAX_VELOCITY / 100.0;
    private Heat heat;
    private Robot robot;

    public Throttle(double powerRatio) {
        this.powerRatio = powerRatio * STANDARD_MAX_VELOCITY / 100.0;
    }

    public PortHandler getSpedometer() {
        return new PortHandler() {
            public short read() {
                return (short) getPower();
            }
        };
    }

    public PortHandler getActuator() {
        return new PortHandler() {
            public void write(short value) {
                setDesiredPower(Math.max(-75, Math.min(100, value)));
            }
        };
    }

    public int getDesiredPower() {
        return desiredPower;
    }

    public void setDesiredPower(int desiredPower) {
        this.desiredPower = desiredPower;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
        updateSpeed();
    }

    private void updateSpeed() {
        double powerRatio = robot.isOverburn() ? this.powerRatio * 1.3 : this.powerRatio;

        speed.setDistanceOverTime((power) * (powerRatio), Duration.ONE_CYCLE);
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public void update(Duration duration) {
        while (duration.getCycles() > 0) {
            if (Math.abs(power - desiredPower) < MAX_ACCELERATION) {
                power = desiredPower;
            } else {
                power += desiredPower > power ? MAX_ACCELERATION : -MAX_ACCELERATION;
            }
            updateSpeed();
            if (Math.abs(power) > 25) {
                heat.cool(Temperature.fromLogScale(.125));
            }
            duration = duration.minus(Duration.ONE_CYCLE);
        }
    }

    public void setHeat(Heat heat) {
        this.heat = heat;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public void shutDown() {
        setDesiredPower(0);
    }
}
