package net.virtualinfinity.atrobots.simulation.atrobot;

import net.virtualinfinity.atrobots.ports.PortHandler;

/**
 * @author Daniel Pitts
 */
public class Heat {
    private Temperature temperature = Temperature.BASE_TEMPERATURE;
    private double coolMultiplier = 1;
    private boolean blockHeat;

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
        if (blockHeat) {
            return;
        }
        this.temperature = this.temperature.minus(temperature.times(coolMultiplier));
        if (this.temperature.compareTo(Temperature.BASE_TEMPERATURE) < 0) {
            this.temperature = Temperature.BASE_TEMPERATURE;
        }
    }

    public void setCoolMultiplier(double coolMultiplier) {
        this.coolMultiplier = coolMultiplier;
    }

    public void blockHeat(boolean blockHeat) {
        this.blockHeat = blockHeat;
    }
}
