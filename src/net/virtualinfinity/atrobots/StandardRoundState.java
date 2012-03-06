package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.arena.Arena;

public class StandardRoundState implements RoundState {
    private final Arena arena;
    private final int totalRounds;
    private final int roundNumber;

    /**
     * @param arena       the arena
     * @param totalRounds the total number of rounds
     * @param roundNumber the current round number.
     */
    public StandardRoundState(Arena arena, int totalRounds, int roundNumber) {
        this.arena = arena;
        this.totalRounds = totalRounds;
        this.roundNumber = roundNumber;
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
}
