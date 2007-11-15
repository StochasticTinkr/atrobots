package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public interface SnapshotVisitor {
    void acceptRobot(RobotSnapshot robotSnapshot);

    void acceptMissile(MissileSnapshot missileSnapshot);

    void acceptMine(MineSnapshot mineSnapshot);

    void acceptExplosion(ExplosionSnapshot explosionSnapshot);

    void acceptScan(ScanSnapshot scanSnapshot);
}
