package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Round {
    private Duration time = Duration.fromCycles(0);
    private Arena arena = new Arena();
    private int number;

    public Round(int number) {
        this.number = number;
    }

    public Duration getTime() {
        return time;
    }

    public Arena getArena() {
        return arena;
    }

    public int getNumber() {
        return number;
    }
}
