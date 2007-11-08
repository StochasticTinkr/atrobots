package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Throttle {
    int power;
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
                setPower(Math.max(-75, Math.min(100, value)));
            }
        };
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
