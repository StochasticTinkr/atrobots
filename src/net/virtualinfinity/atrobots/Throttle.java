package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Throttle {
    int desiredPower;
    int power;
    Speed speed;
    private static final int MAX_ACCELERATION = 4;
    private static final double STANDARD_MAX_VELOCITY = 4.0;

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
        speed.setDistanceOverTime(Distance.fromMeters(power * STANDARD_MAX_VELOCITY / 100.0), Duration.ONE_CYCLE);
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public void update(Duration duration) {
        while (duration.getCycles() > 0) {
            if (Math.abs(power - desiredPower) < MAX_ACCELERATION) {
                power = desiredPower;
            }
            if (power < desiredPower) {
                power += MAX_ACCELERATION;
            }
            if (power > desiredPower) {
                power -= MAX_ACCELERATION;
            }
            updateSpeed();
            duration = duration.minus(Duration.ONE_CYCLE);
        }
    }
}
