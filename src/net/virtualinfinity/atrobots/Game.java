package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.config.Entrants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class coordinates rounds, entrants, and the simulation frame buffer.
 *
 * @author Daniel Pitts
 */
public class Game {
    private Round round;
    private int roundNumber = 0;
    private int totalRounds;
    private int maxProcessorSpeed = 5;
    private SimulationFrameBuffer frameBuffer = new SimulationFrameBuffer();
    private final List<Entrant> entrants = Collections.synchronizedList(new ArrayList<Entrant>());
    private int nextEntrantId;

    public Game(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    /**
     * Get the current round.
     *
     * @return the current round.
     */
    public synchronized Round getRound() {
        return round;
    }

    /**
     * Get the total number of rounds.
     *
     * @return the total number of rounds.
     */
    public synchronized int getTotalRounds() {
        return totalRounds;
    }

//    public synchronized void dispose() {
//    }

    /**
     * Start the next round. This ends the current round.
     */
    public synchronized void nextRound() {
        if (round != null) {
            round.finalizeRound();
        }
        round = new Round(++roundNumber, this, frameBuffer);
        for (Entrant entrant : entrants) {
            round.getArena().addRobot(createRobotFor(entrant));
        }
        round.getArena().buildFrame();

    }

    /**
     * Create a robot for the given entrant.
     *
     * @param entrant the entrant
     * @return the robot.
     */
    protected Robot createRobotFor(Entrant entrant) {
        final Robot robot = entrant.createRobot();
        round.putRobot(entrant, robot);
        return robot;
    }

    /**
     * Add an observer.
     *
     * @param observer the observer to add.
     */
    public synchronized void addSimulationObserver(SimulationObserver observer) {
        frameBuffer.addSimulationObserver(observer);
    }

    /**
     * Remove an observer.
     *
     * @param observer the observer to remove.
     */
    public synchronized void removeSimulationObserver(SimulationObserver observer) {
        frameBuffer.removeSimulationObserver(observer);
    }

    /**
     * Add an entrant for the next round.
     *
     * @param entrant the entrant
     */
    public synchronized void addEntrant(Entrant entrant) {
        entrant.setId(++nextEntrantId);
        entrants.add(entrant);
        entrant.setGame(this);
    }

    /**
     * Extecute one step in the simulation.
     */
    public synchronized void stepRound() {
        getRound().step();

    }

    public void roundOver() {
        nextRound();
    }

    public int getMaxProcessorSpeed() {
        return maxProcessorSpeed;
    }

    public void setMaxProcessorSpeed(int maxProcessorSpeed) {
        this.maxProcessorSpeed = maxProcessorSpeed;
    }

    public void addAll(Entrants entrants) {
        for (Entrant entrant : entrants.getEntrants()) {
            addEntrant(entrant);
        }
    }
}
