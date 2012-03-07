package net.virtualinfinity.atrobots.game;

import net.virtualinfinity.atrobots.arena.SimulationFrameBuffer;
import net.virtualinfinity.atrobots.arena.SimulationObserver;
import net.virtualinfinity.atrobots.compiler.RobotFactory;
import net.virtualinfinity.atrobots.robot.Robot;
import net.virtualinfinity.atrobots.robot.RobotScoreKeeper;

import java.util.*;

/**
 * This class coordinates rounds, entrants, and the simulation frame buffer.
 *
 * @author Daniel Pitts
 */
public class Game implements RoundListener {
    private Round round;
    private int roundNumber = 0;
    private int totalRounds;
    private int maxProcessorSpeed = 5;
    private SimulationFrameBuffer frameBuffer = new SimulationFrameBuffer();
    private final List<RobotFactory> entrants = Collections.synchronizedList(new ArrayList<RobotFactory>());
    private int nextEntrantId;
    private final Map<RobotFactory, RobotScoreKeeper> scoreKeepers = new IdentityHashMap<RobotFactory, RobotScoreKeeper>();

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

        if (roundNumber < getTotalRounds()) {
            round = new Round(++roundNumber, frameBuffer, getTotalRounds());
            round.addRoundListener(this);
            for (RobotFactory entrant : entrants) {
                round.getArena().addRobot(createRobotFor(entrant));
            }
            round.getArena().buildFrame();
        } else {
            round = null;
            gameOver();
        }
    }

    private void gameOver() {

    }

    /**
     * Create a robot for the given entrant.
     *
     * @param entrant the entrant
     * @return the robot.
     */
    protected Robot createRobotFor(RobotFactory entrant) {
        return entrant.createRobot(getRound(), getMaxProcessorSpeed(), getScoreKeeper(entrant));
    }

    public RobotScoreKeeper getScoreKeeper(RobotFactory entrant) {
        RobotScoreKeeper robotScoreKeeper = scoreKeepers.get(entrant);
        if (robotScoreKeeper == null) {
            robotScoreKeeper = new RobotScoreKeeper();
            setScoreKeeper(entrant, robotScoreKeeper);
        }
        return robotScoreKeeper;
    }

    public void setScoreKeeper(RobotFactory entrant, RobotScoreKeeper robotScoreKeeper) {
        scoreKeepers.put(entrant, robotScoreKeeper);
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
    public synchronized void addEntrant(RobotFactory entrant) {
        entrant.setId(++nextEntrantId);
        entrants.add(entrant);
    }

    /**
     * Execute one step in the simulation.
     */
    public synchronized boolean stepRound() {
        if (round == null) {
            return false;
        }
        getRound().step();
        return round != null;
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

}
