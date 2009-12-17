package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.Distance;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.measures.Vector;

import java.util.*;

/**
 * The Arena is the virtual world within which the simulation occurs.
 *
 * @author Daniel Pitts
 */
public class Arena {
    private final List<Robot> robots = new LinkedList<Robot>();
    private final List<Mine> mines = new LinkedList<Mine>();
    private final List<Missile> missiles = new LinkedList<Missile>();
    private final Collection<ArenaObject> others = new LinkedList<ArenaObject>();
    final Collection<? extends Collection<? extends ArenaObject>> allArenaObjectCollections = Arrays.asList(
            missiles,
            mines,
            robots,
            others
    );

    private final RadioDispatcher radioDispatcher = new RadioDispatcher();
    private SimulationFrameBuffer simulationFrameBuffer;

    /**
     * Get the number of robots still active in the arena.
     *
     * @return the number of robots still active in the arena.
     */
    public int countActiveRobots() {
        return robots.size();
    }

    /**
     * Places a mine into this virtual arena.
     *
     * @param mine the mine to place in the arena.
     */
    public void placeMine(Mine mine) {
        connectArena(mine);
        mines.add(mine);
    }

    private void connectArena(ArenaObject object) {
        object.setArena(this);
    }

    /**
     * Count the number of live mines which were layed by the given mine layer.
     *
     * @param mineLayer the mine layer.
     * @return the number of live mines in this arena which were layed by the given mine layer.
     */
    public int countMinesLayedBy(MineLayer mineLayer) {
        int count = 0;
        for (Mine mine : mines) {
            if (!mine.isDead() && mine.layedBy(mineLayer)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get the radio dispatcher for this arena.
     *
     * @return the radio dispatcher for this arena.
     */
    public RadioDispatcher getRadioDispatcher() {
        return radioDispatcher;
    }

    /**
     * Scan for the closest robot which is in the given arc.
     *
     * @param ignore       the source robot, which should be ignored.
     * @param position     the vertex of the arc.
     * @param angleBracket the bracket which defines the scan arc.
     * @param maxDistance  the radius of the scan arc.
     * @return the result of the scan.
     */
    public ScanResult scan(Robot ignore, Position position, final AngleBracket angleBracket, final Distance maxDistance) {
        final ScanResult scanResult = calculateResult(ignore, position, angleBracket, maxDistance);
        final ScanParameters object = new ScanParameters(angleBracket, maxDistance, scanResult.successful(), scanResult.getMatchPositionVector());
        others.add(object);
        object.getPosition().copyFrom(position);
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
        if (closest != null && closestDistance.compareTo(maxDistance) <= 0) {
            return new ScanResult(closest, closestDistance, vectorToClosest.getAngle());
        }
        return new ScanResult();
    }

    /**
     * Simulate a certain amount of time elapsing.
     *
     * @param time the amount of time.
     */
    public void simulate(Duration time) {
        updateSimulation(time);
        buildFrame();
    }

    /**
     * Prepare a snapshot of the current arena state in the {@link net.virtualinfinity.atrobots.SimulationFrameBuffer}.
     */
    public void buildFrame() {
        simulationFrameBuffer.beginFrame();
        for (Collection<? extends ArenaObject> objectCollection : allArenaObjectCollections) {
            for (ArenaObject object : objectCollection) {
                simulationFrameBuffer.addObject(object.getSnapshot());
            }
        }
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
            checkCollissions();
            removeDead();
        }
    }

    private void removeDead() {
        for (Collection<? extends ArenaObject> objectCollection : allArenaObjectCollections) {
            for (Iterator<? extends ArenaObject> it = objectCollection.iterator(); it.hasNext();) {
                if (it.next().isDead()) {
                    it.remove();
                }
            }
        }
    }

    private void checkCollissions() {
        for (int i = 0; i < robots.size(); ++i) {
            final Robot robot = robots.get(i);
            for (int j = 0; j < i; ++j) {
                robots.get(j).checkCollision(robot);
            }
            for (Mine mine : mines) {
                mine.checkCollision(robot);
            }
            for (Missile missile : missiles) {
                missile.checkCollision(robot);
            }
        }
    }

    /**
     * The the frame buffer to use.
     *
     * @param simulationFrameBuffer the frame buffer.
     */
    public void setSimulationFrameBuffer(SimulationFrameBuffer simulationFrameBuffer) {
        this.simulationFrameBuffer = simulationFrameBuffer;
    }

    /**
     * Add a robot to the arena at a random location.
     *
     * @param robot the robot to add to this arena.
     */
    public void addRobot(Robot robot) {
        robot.getPosition().copyFrom(Position.random(0.0, 0.0, 1000.0, 1000.0));
        connectArena(robot);
        robots.add(robot);
    }

    /**
     * File a missile within the arena.
     *
     * @param missile the missle.
     */
    public void fireMissile(Missile missile) {
        connectArena(missile);
        missiles.add(missile);
    }

    /**
     * Cause an explosion.
     *
     * @param cause             the robot which gets credit for any damage done.
     * @param explosionFunction the damage explosion function.
     */
    public void explosion(Robot cause, ExplosionFunction explosionFunction) {
        others.add(new Explosion(explosionFunction.getCenter(), explosionFunction.getRadius()));
        for (Robot robot : robots) {
            explosionFunction.inflictDamage(cause, robot);
        }
    }
}
