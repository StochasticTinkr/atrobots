package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public interface SimulationObserver {
    void frameAvailable(SimulationFrameBuffer runnable);
}
