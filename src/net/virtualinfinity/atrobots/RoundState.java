package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.simulation.arena.Arena;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public interface RoundState {
    Arena getArena();

    int getTotalRounds();

    int getRoundNumber();
}
