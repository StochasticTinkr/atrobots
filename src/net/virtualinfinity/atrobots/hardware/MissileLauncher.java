package net.virtualinfinity.atrobots.hardware;

import net.virtualinfinity.atrobots.measures.AbsoluteAngle;
import net.virtualinfinity.atrobots.measures.RelativeAngle;
import net.virtualinfinity.atrobots.measures.Temperature;
import net.virtualinfinity.atrobots.ports.PortHandler;
import net.virtualinfinity.atrobots.simulation.arena.Arena;
import net.virtualinfinity.atrobots.simulation.arena.Heading;
import net.virtualinfinity.atrobots.simulation.arena.Position;
import net.virtualinfinity.atrobots.simulation.atrobot.HasOverburner;
import net.virtualinfinity.atrobots.simulation.atrobot.MissileFactory;
import net.virtualinfinity.atrobots.simulation.missile.Missile;

/**
 * @author Daniel Pitts
 */
public class MissileLauncher {
    private Heading heading;
    private double power;
    private Position position;
    private HeatSinks heatSinks;
    private MissileFactory missileFactory;
    private HasOverburner overburner;
    private Arena arena;

    public MissileLauncher() {
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PortHandler getActuator() {
        return new PortHandler() {
            public void write(short value) {
                consumeCycles(3);
                fireMissile(RelativeAngle.fromBygrees(Math.max(-4, Math.min(value, 4))));
            }
        };
    }

    private void fireMissile(RelativeAngle shift) {
        getArena().fireMissile(createMissile(shift));
        getHeatSinks().warm(getFiringTempurature());
    }

    private AbsoluteAngle getMissileHeading(RelativeAngle shift) {
        return heading.getAngle().counterClockwise(shift);
    }

    private Temperature getFiringTempurature() {
        return Temperature.fromLogScale(overburner.isOverburn() ? 20 : 30);
    }

    private Missile createMissile(RelativeAngle shift) {
        return missileFactory.createMissile(getMissileHeading(shift), position, getPower());
    }

    private double getPower() {
        return overburner.isOverburn() ? power * 1.30 : power;
    }

    public HeatSinks getHeatSinks() {
        return heatSinks;
    }

    public void setHeatSinks(HeatSinks heatSinks) {
        this.heatSinks = heatSinks;
    }

    public MissileFactory getMissileFactory() {
        return missileFactory;
    }

    public void setMissileFactory(MissileFactory missileFactory) {
        this.missileFactory = missileFactory;
    }

    public HasOverburner getOverburner() {
        return overburner;
    }

    public void setOverburner(HasOverburner overburner) {
        this.overburner = overburner;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }
}
