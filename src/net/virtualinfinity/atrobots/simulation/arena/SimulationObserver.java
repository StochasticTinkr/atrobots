package net.virtualinfinity.atrobots.simulation.arena;

/**
 * @author Daniel Pitts
 */
public interface SimulationObserver {
    void frameAvailable(SimulationFrameBuffer frameBuffer);
}
