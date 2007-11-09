package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Heat {
    private Temperature temperature;

    public PortHandler getHeatSensor() {
        return new PortHandler() {
            public short read() {
                return (short) temperature.getLogScale();
            }
        };
    }

    public Temperature getTemperature() {
        return temperature;
    }
}
