package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.simulation.arena.Arena;

public class RoundState {
    private final Arena arena;
    private final int totalRounds;
    private final int roundNumber;
    private final int maxProcessorSpeed;

    /**
     * @param arena             the arena
     * @param totalRounds       the total number of rounds
     * @param roundNumber       the current round number.
     * @param maxProcessorSpeed the maximum allowed processor speed.
     */
    public RoundState(Arena arena, int totalRounds, int roundNumber, int maxProcessorSpeed) {
        this.arena = arena;
        this.totalRounds = totalRounds;
        this.roundNumber = roundNumber;
        this.maxProcessorSpeed = maxProcessorSpeed;
    }

    public Arena getArena() {
        return arena;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getMaxProcessorSpeed() {
        return maxProcessorSpeed;
    }
}
