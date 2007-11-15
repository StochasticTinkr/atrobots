package net.virtualinfinity.atrobots.measures;

/**
 * @author Daniel Pitts
 */
public final class Duration {
    private final int cycles;
    public static final Duration ONE_CYCLE = Duration.fromCycles(1);

    private Duration(int cycles) {
        this.cycles = cycles;
    }

    public double divided(Duration duration) {
        return ((double) cycles) / ((double) duration.cycles);
    }

    public int getCycles() {
        return cycles;
    }

    public static Duration fromCycles(int cycles) {
        return new Duration(cycles);
    }

    public Duration minus(Duration duration) {
        return new Duration(getCycles() - duration.getCycles());
    }

    public String toString() {
        return cycles + "s";
    }
}
