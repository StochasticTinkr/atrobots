package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.Robot;
import net.virtualinfinity.atrobots.computer.MemoryCell;

/**
 * @author Daniel Pitts
 */
public class GetRobotStatisticsInterrupt extends InterruptHandler {
    private final Robot entrant;
    private final MemoryCell totalKills;
    private final MemoryCell roundKills;
    private final MemoryCell totalDeaths;

    public GetRobotStatisticsInterrupt(Robot robot, MemoryCell totalKills, MemoryCell roundKills, MemoryCell totalDeaths) {
        this.entrant = robot;
        this.totalKills = totalKills;
        this.roundKills = roundKills;
        this.totalDeaths = totalDeaths;
    }

    public void handleInterrupt() {
        totalKills.set((short) entrant.getTotalKills());
        roundKills.set((short) entrant.getRoundKills());
        totalDeaths.set((short) entrant.getTotalDeaths());
    }
}
