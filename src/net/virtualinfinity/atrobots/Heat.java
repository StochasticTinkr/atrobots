package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Heat {
    private Temperature temperature = Temperature.BASE_TEMPERATURE;

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

    public void warm(Temperature temperature) {
        this.temperature = this.temperature.plus(temperature);
    }

    public void cool(Temperature temperature) {
        this.temperature = this.temperature.minus(temperature);
        if (this.temperature.compareTo(Temperature.BASE_TEMPERATURE) < 0) {
            this.temperature = Temperature.BASE_TEMPERATURE;
        }
    }
}
