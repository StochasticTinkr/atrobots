package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Temperature {
    private final double celsius;

    public static final Temperature BASE_TEMPERATURE = fromCelsius(20);
    private static final Temperature ROBOT_DEATH = fromCelsius(750);
    private static final int LOG_SCALE_ROBOT_DEATH = 500;
    private static final double LOG_ROBOT_DEATH = Math.log(ROBOT_DEATH.getCelsius());
    private static final double LOG_SCALER = LOG_ROBOT_DEATH / LOG_SCALE_ROBOT_DEATH;

    private Temperature(double celsius) {
        this.celsius = celsius;
    }

    public int getLogScale() {
        return (int) Math.round(Math.log(getCelsius() - BASE_TEMPERATURE.getCelsius()) / LOG_SCALER);
    }

    public static Temperature fromCelsius(double celsius) {
        return new Temperature(celsius);
    }

    public double getCelsius() {
        return celsius;
    }

    public static Temperature fromLogScale(int logTemp) {
        return fromCelsius(Math.exp(logTemp * LOG_SCALER) + BASE_TEMPERATURE.getCelsius());
    }
}
