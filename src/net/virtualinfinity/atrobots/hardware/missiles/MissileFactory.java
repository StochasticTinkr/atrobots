package net.virtualinfinity.atrobots.hardware.missiles;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.simulation.arena.Position;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public interface MissileFactory {
    Missile createMissile(AbsoluteAngle missileHeading, Position position, double power);
}
