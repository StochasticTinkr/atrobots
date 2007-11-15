package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.Entrant;
import net.virtualinfinity.atrobots.MemoryCell;

/**
 * @author Daniel Pitts
 */
public class GetRobotStatisticsInterrupt extends InterruptHandler {
    private final Entrant entrant;
    private final MemoryCell totalKills;
    private final MemoryCell roundKills;
    private final MemoryCell totalDeaths;

    public GetRobotStatisticsInterrupt(Entrant entrant, MemoryCell totalKills, MemoryCell roundKills, MemoryCell totalDeaths) {
        this.entrant = entrant;
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
