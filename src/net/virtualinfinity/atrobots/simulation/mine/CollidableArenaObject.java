package net.virtualinfinity.atrobots.simulation.mine;

import net.virtualinfinity.atrobots.simulation.arena.ArenaObject;
import net.virtualinfinity.atrobots.simulation.arena.TangibleArenaObject;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public abstract class CollidableArenaObject extends ArenaObject {
    public abstract void checkCollision(TangibleArenaObject arenaObject);
}
