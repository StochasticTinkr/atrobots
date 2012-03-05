package net.virtualinfinity.atrobots.simulation.arena;

import net.virtualinfinity.atrobots.snapshots.ArenaObjectSnapshot;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;
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
        private final boolean roundOver;

        public SimulationFrame(Collection<ArenaObjectSnapshot> allObjects, Collection<RobotSnapshot> robots, boolean roundOver) {
            this.allObjects = allObjects;
            this.robots = robots;
            this.roundOver = roundOver;
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
        }

        public Collection<ArenaObjectSnapshot> getAllObjects() {
            return allObjects;
        }

        public void addRobot(RobotSnapshot snapshot) {
            robots.add(snapshot);
            add(snapshot);
        }

        public boolean isRoundOver() {
            return roundOver;
        }
    }

    private SimulationFrame frameToBuild;
    private volatile SimulationFrame currentFrame;

    public void beginFrame(boolean roundOver) {
        frameToBuild = new SimulationFrame(new ArrayList<ArenaObjectSnapshot>(), new ArrayList<RobotSnapshot>(), roundOver);
    }

    public void addObject(ArenaObjectSnapshot snapshot) {
        frameToBuild.add(snapshot);
    }

    public void addRobot(RobotSnapshot snapshot) {
        frameToBuild.addRobot(snapshot);
    }

    public void endFrame() {
        currentFrame = frameToBuild;
        for (SimulationObserver observer : observers) {
            observer.frameAvailable(SimulationFrameBuffer.this);
        }
    }

    public void roundEnded() {
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
