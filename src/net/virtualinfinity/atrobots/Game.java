package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Game {
    private Round round;
    private int roundNumber = 0;
    private int totalRounds;
    private SimulationFrameBuffer frameBuffer = new SimulationFrameBuffer();

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

    public void nextRound() {
        round = new Round(++roundNumber);
        round.getArena().setSimulationFrameBuffer(frameBuffer);
    }

    public void addSimulationObserver(SimulationObserver observer) {
        frameBuffer.addSimulationObserver(observer);
    }
}
