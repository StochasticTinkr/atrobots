package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.simulation.arena.Arena;
import net.virtualinfinity.atrobots.simulation.arena.SimulationFrameBuffer;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class Round implements RoundState {
    private Duration roundEnd = Duration.fromCycles(10);
    private Duration maxCycles = Duration.fromCycles(25000);
    private Arena arena;
    private int number;
    private final List<RoundListener> roundListeners = new ArrayList<RoundListener>();
    private Map<Entrant, Robot> robots = new HashMap<Entrant, Robot>();
    private final int totalRounds;

    public Round(int number, SimulationFrameBuffer frameBuffer, int totalRounds) {
        this.number = number;
        this.totalRounds = totalRounds;
        arena = new Arena();
        arena.setSimulationFrameBuffer(frameBuffer);
    }

    public void addRoundListener(RoundListener roundListener) {
        roundListeners.add(roundListener);
    }

    public Arena getArena() {
        return arena;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public int getRoundNumber() {
        return number;
    }

    public void step() {
        if (arena.countActiveRobots() == 1) {
            roundEnd = roundEnd.minus(Duration.ONE_CYCLE);
        }
        if (roundEnd.getCycles() == 0 || arena.countActiveRobots() == 0 || arena.getTime().compareTo(maxCycles) > 0) {
            for (RoundListener roundListener : roundListeners) {
                roundListener.roundOver();
            }
            return;
        }
        arena.simulate();

    }

    public void putRobot(Entrant entrant, Robot robot) {
        robots.put(entrant, robot);
    }

    public Robot getRobot(Entrant entrant) {
        return robots.get(entrant);
    }

    public void finalizeRound() {
        arena.endRound();
    }
}
