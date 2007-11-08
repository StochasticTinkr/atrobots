package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Game {
    private Round round;
    private int totalRounds;

    public Game(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    public Round getRound() {
        return round;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public void dispose() {
    }
}
