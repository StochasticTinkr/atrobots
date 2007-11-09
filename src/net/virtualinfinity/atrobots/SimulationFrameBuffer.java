package net.virtualinfinity.atrobots;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Daniel Pitts
 */
public class SimulationFrameBuffer {
    private Collection<SimulationObserver> observers = new ArrayList<SimulationObserver>();
    private Collection<ArenaObjectSnapshot> frameToBuild;
    private volatile Collection<ArenaObjectSnapshot> currentFrame;

    public void beginFrame() {
        frameToBuild = new ArrayList<ArenaObjectSnapshot>();
    }

    public void addObject(ArenaObjectSnapshot snapshot) {
        frameToBuild.add(snapshot);
    }

    public void endFrame() {
        currentFrame = frameToBuild;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                for (SimulationObserver observer : observers) {
                    observer.frameAvailable(SimulationFrameBuffer.this);
                }
            }
        });
    }

    public Collection<ArenaObjectSnapshot> getCurrentFrame() {
        return currentFrame;
    }

    public void addSimulationObserver(final SimulationObserver observer) {
        if (EventQueue.isDispatchThread()) {
            observers.add(observer);
        } else {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    addSimulationObserver(observer);
                }
            });
        }
    }

    public void removeSimulationObserver(SimulationObserver observer) {
        observers.remove(observer);
    }
}
