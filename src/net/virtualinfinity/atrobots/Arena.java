package net.virtualinfinity.atrobots;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Daniel Pitts
 */
public class Arena {
    private final Collection<Robot> robots = new ArrayList<Robot>();
    private final Collection<Mine> mines = new ArrayList<Mine>();
    private final Collection<Missile> missiles = new ArrayList<Missile>();
    private final RadioDispatcher radioDispatcher = new RadioDispatcher();

    Collection<? extends Collection<? extends ArenaObject>> allArenaObjectCollections =
            Arrays.asList(mines, robots, missiles);
    private SimulationFrameBuffer simulationFrameBuffer;
    private Collection<ArenaObjectSnapshot> scans = new ArrayList<ArenaObjectSnapshot>();

    public int countActiveRobots() {
        return robots.size();
    }

    public void placeMine(Mine mine) {
        mines.add(mine);
    }

    public int countMinesLayedBy(MineLayer mineLayer) {
        int count = 0;
        for (Mine mine : mines) {
            if (!mine.isDead() && mine.layedBy(mineLayer)) {
                count++;
            }
        }
        return count;
    }

    public RadioDispatcher getRadioDispatcher() {
        return radioDispatcher;
    }


    public ScanResult scan(Robot ignore, Position position, final AngleBracket angleBracket, final Distance maxDistance) {
        final ScanResult scanResult = calculateResult(ignore, position, angleBracket, maxDistance);
        final ArenaObjectSnapshot objectSnapshot = new ArenaObjectSnapshot() {
            public void paint(Graphics2D g2d) {
                g2d.setPaint(Color.white);
                g2d.draw(angleBracket.toShape(positionVector.getX(), positionVector.getY(), maxDistance));
            }
        };
        objectSnapshot.setPositionVector(position.getVector());
        scans.add(objectSnapshot);
        return scanResult;
    }

    private ScanResult calculateResult(Robot ignore, Position position, AngleBracket angleBracket, Distance maxDistance) {
        Vector vectorToClosest = null;
        Distance closestDistance = maxDistance;
        Robot closest = null;
        for (Robot robot : robots) {
            if (robot == ignore) {
                continue;
            }
            final Vector vector = robot.getPosition().getVectorTo(position);
            final Distance distance = vector.getMagnatude();
            if (closest == null || distance.compareTo(closestDistance) < 0) {
                if (angleBracket.contains(vector.getAngle())) {
                    closest = robot;
                    closestDistance = distance;
                    vectorToClosest = vector;
                }
            }
        }
        if (closest != null) {
            return new ScanResult(closest, closestDistance, vectorToClosest.getAngle());
        }
        return new ScanResult();
    }

    public void simulate(Duration time) {
        updateSimulation(time);
        buildFrame();
    }

    public void buildFrame() {
        simulationFrameBuffer.beginFrame();
        for (Collection<? extends ArenaObject> objectCollection : allArenaObjectCollections) {
            for (ArenaObject object : objectCollection) {
                simulationFrameBuffer.addObject(object.getSnapshot());
            }
        }
        for (ArenaObjectSnapshot snapshot : scans) {
            simulationFrameBuffer.addObject(snapshot);
        }
        scans.clear();
        simulationFrameBuffer.endFrame();
    }

    private void updateSimulation(Duration time) {
        while (time.getCycles() > 0) {
            time = time.minus(Duration.ONE_CYCLE);
            for (Collection<? extends ArenaObject> objectCollection : allArenaObjectCollections) {
                for (ArenaObject object : objectCollection) {
                    object.update(Duration.ONE_CYCLE);
                }
            }
        }
    }

    public void setSimulationFrameBuffer(SimulationFrameBuffer simulationFrameBuffer) {
        this.simulationFrameBuffer = simulationFrameBuffer;
    }

    public void addRobot(Robot robot) {
        robot.getPosition().copyFrom(Position.random(0.0, 0.0, 1000.0, 1000.0));
        robots.add(robot);
    }

    public void fireMissile(Missile missile) {
        missiles.add(missile);
    }
}
