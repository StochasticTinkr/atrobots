package net.virtualinfinity.atrobots.simulation.atrobot;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.simulation.arena.Position;
import net.virtualinfinity.atrobots.simulation.missile.Missile;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public interface MissileFactory {
    Missile createMissile(AbsoluteAngle missileHeading, Position position, double power);
}
