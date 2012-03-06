package net.virtualinfinity.atrobots.arena;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public abstract class CollidableArenaObject extends ArenaObject {
    public abstract void checkCollision(TangibleArenaObject arenaObject);
}
