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

    public synchronized Round getRound() {
        return round;
    }

    public synchronized int getTotalRounds() {
        return totalRounds;
    }

    public synchronized void dispose() {
    }

    public synchronized void nextRound() {
        round = new Round(++roundNumber);
        round.getArena().setSimulationFrameBuffer(frameBuffer);
        for (Entrant entrant : entrants) {
            final Robot robot = entrant.createRobot();
            round.getArena().addRobot(robot);
        }
        round.getArena().buildFrame();

    }

    public synchronized void addSimulationObserver(SimulationObserver observer) {
        frameBuffer.addSimulationObserver(observer);
    }

    public synchronized void removeSimulationObserver(SimulationObserver observer) {
        frameBuffer.removeSimulationObserver(observer);
    }

    public synchronized void addEntrant(Entrant entrant) {
        entrant.setId(entrants.size());
        entrants.add(entrant);
        entrant.setGame(this);
    }

    public synchronized void stepRound() {
        getRound().step();
    }

    public List<Entrant> getEntrants() {
        return new ArrayList<Entrant>(entrants);
    }
}
