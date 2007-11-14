package net.virtualinfinity.atrobots;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Pitts
 */
public class Game {
    private Round round;
    private int roundNumber = 0;
    private int totalRounds;
    private SimulationFrameBuffer frameBuffer = new SimulationFrameBuffer();
    private final List<Entrant> entrants = Collections.synchronizedList(new ArrayList<Entrant>());

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
        for (Entrant entrant : entrants) {
            final Robot robot = entrant.createRobot();
            round.getArena().addRobot(robot);
        }
        round.getArena().buildFrame();

    }

    public void addSimulationObserver(SimulationObserver observer) {
        frameBuffer.addSimulationObserver(observer);
    }

    public void removeSimulationObserver(SimulationObserver observer) {
        frameBuffer.removeSimulationObserver(observer);
    }

    public void addEntrant(Entrant entrant) {
        entrants.add(entrant);
        entrant.setGame(this);
    }

    public synchronized void stepRound() {
        getRound().step();
    }
}
