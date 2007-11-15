package net.virtualinfinity.atrobots.snapshots;

import net.virtualinfinity.atrobots.Temperature;
import net.virtualinfinity.atrobots.measures.AbsoluteAngle;

/**
 * @author Daniel Pitts
 */
public class RobotSnapshot extends ArenaObjectSnapshot {
    private Temperature temperature;
    private double armor;
    private boolean overburn;
    private boolean activeShield;
    private AbsoluteAngle heading;
    private AbsoluteAngle turretHeading;
    private String name;
    private int id;
    private String lastMessage;

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public void setOverburn(boolean overburn) {
        this.overburn = overburn;
    }

    public void setActiveShield(boolean activeShield) {
        this.activeShield = activeShield;
    }

    public void visit(SnapshotVisitor visitor) {
        visitor.acceptRobot(this);
    }

    public void setHeading(AbsoluteAngle heading) {
        this.heading = heading;
    }

    public void setTurretHeading(AbsoluteAngle turretHeading) {
        this.turretHeading = turretHeading;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public double getArmor() {
        return armor;
    }

    public boolean isOverburn() {
        return overburn;
    }

    public boolean isActiveShield() {
        return activeShield;
    }

    public AbsoluteAngle getHeading() {
        return heading;
    }

    public AbsoluteAngle getTurretHeading() {
        return turretHeading;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
