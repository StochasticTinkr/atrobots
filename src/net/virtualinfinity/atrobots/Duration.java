package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public final class Duration {
    private final int cycles;

    private Duration(int cycles) {
        this.cycles = cycles;
    }

    public double divided(Duration duration) {
        return ((double)cycles) / ((double)duration.cycles);
    }

    public int getCycles() {
        return cycles;
    }

    public static Duration fromCycles(int cycles) {
        return new Duration(cycles);
    }
}
