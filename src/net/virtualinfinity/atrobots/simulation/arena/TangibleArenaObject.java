package net.virtualinfinity.atrobots.simulation.arena;

import net.virtualinfinity.atrobots.hardware.Transponder;
import net.virtualinfinity.atrobots.simulation.atrobot.DamageInflicter;
import net.virtualinfinity.atrobots.simulation.mine.CollidableArenaObject;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public abstract class TangibleArenaObject extends CollidableArenaObject {
    public abstract Transponder getTransponder();

    public abstract void checkCollision(TangibleArenaObject collisionTarget);

    public abstract void inflictDamage(DamageInflicter cause, double amount);

    public abstract void collides();

    public abstract void winRound();

    public abstract void tieRound();
}
