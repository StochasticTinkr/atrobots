package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
import net.virtualinfinity.atrobots.snapshots.SnapshotAdaptor;
import net.virtualinfinity.atrobots.snapshots.SnapshotVisitor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Daniel Pitts
 */
public class SimulationFrameBuffer {
    private Collection<SimulationObserver> observers = new ArrayList<SimulationObserver>();

    public static class SimulationFrame {
        private final Collection<ArenaObjectSnapshot> allObjects;
        private final Collection<RobotSnapshot> robots;

        public SimulationFrame(Collection<ArenaObjectSnapshot> allObjects, Collection<RobotSnapshot> robots) {
            this.allObjects = allObjects;
            this.robots = robots;
        }

        public void visitAll(SnapshotVisitor visitor) {
            for (ArenaObjectSnapshot snapshot : allObjects) {
                snapshot.visit(visitor);
            }
        }

        public void visitRobots(SnapshotVisitor visitor) {
            for (RobotSnapshot snapshot : robots) {
                snapshot.visit(visitor);
            }
        }

        void add(ArenaObjectSnapshot snapshot) {
            allObjects.add(snapshot);
            snapshot.visit(new RobotSegregator());
        }

        public Collection<ArenaObjectSnapshot> getAllObjects() {
            return allObjects;
        }

        private class RobotSegregator extends SnapshotAdaptor {
            @Override
            public void acceptRobot(RobotSnapshot robotSnapshot) {
                robots.add(robotSnapshot);
            }
        }
    }

    private SimulationFrame frameToBuild;
    private volatile SimulationFrame currentFrame;

    public void beginFrame() {
        frameToBuild = new SimulationFrame(new ArrayList<ArenaObjectSnapshot>(), new ArrayList<RobotSnapshot>());
    }

    public void addObject(ArenaObjectSnapshot snapshot) {
        frameToBuild.add(snapshot);
    }

    public void endFrame() {
        currentFrame = frameToBuild;
        for (SimulationObserver observer : observers) {
            observer.frameAvailable(SimulationFrameBuffer.this);
        }
    }

    public SimulationFrame getCurrentFrame() {
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
