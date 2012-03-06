package net.virtualinfinity.atrobots.game;

import net.virtualinfinity.atrobots.arena.Arena;
import net.virtualinfinity.atrobots.arena.RoundState;
import net.virtualinfinity.atrobots.arena.SimulationFrameBuffer;
import net.virtualinfinity.atrobots.compiler.RobotFactory;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.robot.Robot;

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
    private Map<RobotFactory, Robot> robots = new HashMap<RobotFactory, Robot>();
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
        if (arena.isOnlyOneRobotAlive()) {
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


    public void finalizeRound() {
        arena.endRound();
    }
}