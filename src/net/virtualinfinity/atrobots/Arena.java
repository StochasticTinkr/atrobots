package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.measures.AngleBracket;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.snapshots.RobotSnapshot;

import java.util.*;

/**
 * The Arena is the virtual world within which the simulation occurs.
 *
 * @author Daniel Pitts
 */
public class Arena {
    private final List<Robot> activeRobots = new LinkedList<Robot>();
    private final List<Robot> allRobots = new LinkedList<Robot>();
    private final List<Mine> mines = new LinkedList<Mine>();
    private final List<Missile> missiles = new LinkedList<Missile>();
    private final Collection<ArenaObject> others = new LinkedList<ArenaObject>();
    final Collection<Collection<? extends ArenaObject>> allArenaObjectCollections = new ArrayList<Collection<? extends ArenaObject>>();

    {
        allArenaObjectCollections.add(missiles);
        allArenaObjectCollections.add(mines);
        allArenaObjectCollections.add(activeRobots);
        allArenaObjectCollections.add(others);
    }

    final Collection<Collection<? extends ArenaObject>> nonRobots = new ArrayList<Collection<? extends ArenaObject>>();

    {
        nonRobots.add(missiles);
        nonRobots.add(mines);
        nonRobots.add(others);
    }

    private final RadioDispatcher radioDispatcher = new RadioDispatcher();
    private SimulationFrameBuffer simulationFrameBuffer;

    /**
     * Get the number of robots still active in the arena.
     *
     * @return the number of robots still active in the arena.
     */
    public int countActiveRobots() {
        return activeRobots.size();
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
     * @param ignore            the source robot, which should be ignored.
     * @param position          the vertex of the arc.
     * @param angleBracket      the bracket which defines the scan arc.
     * @param maxDistance       the radius of the scan arc.
     * @param calculateAccuracy whether the accuracy should be calculated or not.
     * @return the result of the scan.
     */
    public ScanResult scan(Robot ignore, Position position, final AngleBracket angleBracket, final double maxDistance, boolean calculateAccuracy) {
        final ScanResult scanResult = calculateResult(ignore, position, angleBracket, maxDistance, calculateAccuracy);
        final ScanParameters object = new ScanParameters(angleBracket, maxDistance, scanResult.successful(), scanResult.getMatchPositionVector(), calculateAccuracy && scanResult.successful(), scanResult.getAccuracy());
        others.add(object);
        object.getPosition().copyFrom(position);
        return scanResult;
    }

    private ScanResult calculateResult(Robot ignore, Position position, AngleBracket angleBracket, double maxDistance, boolean calculateAccuracy) {
        ScanWork scanWork = new ScanWork(position, angleBracket, maxDistance, calculateAccuracy);
        for (Robot robot : activeRobots) {
            if (robot != ignore) {
                scanWork.visit(robot);
            }
        }
        return scanWork.toScanResult();
    }

    /**
     * Simulate a certain amount of time elapsing.
     */
    public void simulate() {
        updateSimulation();
        buildFrame();
    }

    /**
     * Prepare a snapshot of the current arena state in the {@link net.virtualinfinity.atrobots.SimulationFrameBuffer}.
     */
    public void buildFrame() {
        simulationFrameBuffer.beginFrame();
        for (Collection<? extends ArenaObject> objectCollection : nonRobots) {
            for (ArenaObject object : objectCollection) {
                simulationFrameBuffer.addObject(object.getSnapshot());
            }
        }
        for (Robot robot : allRobots) {
            simulationFrameBuffer.addRobot((RobotSnapshot) robot.getSnapshot());
        }
        simulationFrameBuffer.endFrame();
    }

    private void updateSimulation() {
        for (Collection<? extends ArenaObject> objectCollection : allArenaObjectCollections) {
            for (ArenaObject object : objectCollection) {
                object.update(Duration.ONE_CYCLE);
            }
        }
        checkCollissions();
        removeDead();
    }

    private void removeDead() {
        for (Collection<? extends ArenaObject> objectCollection : allArenaObjectCollections) {
            for (Iterator<? extends ArenaObject> it = objectCollection.iterator(); it.hasNext(); ) {
                if (it.next().isDead()) {
                    it.remove();
                }
            }
        }
    }

    private void checkCollissions() {
        for (final Robot collisionTarget : activeRobots) {
            for (Robot robot : activeRobots) {
                if (robot == collisionTarget) {
                    break;
                }
                robot.checkCollision(collisionTarget);
            }
            for (Mine mine : mines) {
                mine.checkCollision(collisionTarget);
            }
            for (Missile missile : missiles) {
                missile.checkCollision(collisionTarget);
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
        activeRobots.add(robot);
        allRobots.add(robot);
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
        for (Robot robot : activeRobots) {
            explosionFunction.inflictDamage(cause, robot);
        }
    }

    public void determineWinners() {
        if (!activeRobots.isEmpty()) {
            if (activeRobots.size() == 1) {
                for (Robot robot : activeRobots) {
                    robot.winRound();
                }
            } else {
                for (Robot robot : activeRobots) {
                    robot.tieRound();
                }
            }
        } else {
            for (Robot robot : allRobots) {
                robot.tieRound();
            }
        }
    }
}
