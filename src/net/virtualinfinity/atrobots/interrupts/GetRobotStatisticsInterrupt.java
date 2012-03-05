package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.Robot;
import net.virtualinfinity.atrobots.computer.MemoryCell;

/**
 * @author Daniel Pitts
 */
public class GetRobotStatisticsInterrupt extends InterruptHandler {
    private final Robot robot;
    private final MemoryCell totalKills;
    private final MemoryCell roundKills;
    private final MemoryCell totalDeaths;

    public GetRobotStatisticsInterrupt(Robot robot, MemoryCell totalKills, MemoryCell roundKills, MemoryCell totalDeaths) {
        this.robot = robot;
        this.totalKills = totalKills;
        this.roundKills = roundKills;
        this.totalDeaths = totalDeaths;
    }

    public void handleInterrupt() {
        totalKills.set((short) robot.getTotalKills());
        roundKills.set((short) robot.getRoundKills());
        totalDeaths.set((short) robot.getTotalDeaths());
    }
}
