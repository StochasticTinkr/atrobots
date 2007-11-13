package net.virtualinfinity.atrobots;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Daniel Pitts
 */
public class Arena {
    private final List<Robot> robots = new LinkedList<Robot>();
    private final List<Mine> mines = new LinkedList<Mine>();
    private final List<Missile> missiles = new LinkedList<Missile>();
    private final Collection<Explosion> explosions = new ArrayList<Explosion>();
    Collection<? extends Collection<? extends ArenaObject>> allArenaObjectCollections =
            Arrays.asList(mines, robots, missiles, explosions);

    private final RadioDispatcher radioDispatcher = new RadioDispatcher();
    private SimulationFrameBuffer simulationFrameBuffer;
    private Collection<ArenaObjectSnapshot> scans = new ArrayList<ArenaObjectSnapshot>();

    public int countActiveRobots() {
        return robots.size();
    }

    public void placeMine(Mine mine) {
        connectArena(mine);
        mines.add(mine);
    }

    private void connectArena(ArenaObject object) {
        object.setArena(this);
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
        final ArenaObjectSnapshot objectSnapshot = new ScanSnapshot(scanResult, angleBracket, maxDistance);
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

    public void setSimulationFrameBuffer(SimulationFrameBuffer simulationFrameBuffer) {
        this.simulationFrameBuffer = simulationFrameBuffer;
    }

    public void addRobot(Robot robot) {
        robot.getPosition().copyFrom(Position.random(0.0, 0.0, 1000.0, 1000.0));
        connectArena(robot);
        robots.add(robot);
    }

    public void fireMissile(Missile missile) {
        connectArena(missile);
        missiles.add(missile);
    }

    public void explosion(Robot cause, ExplosionFunction explosionFunction) {
        explosions.add(new Explosion(explosionFunction.getCenter(), explosionFunction.getRadius()));
        for (Robot robot : robots) {
            explosionFunction.inflictDamage(cause, robot);
        }
    }


    private static class ScanSnapshot extends ArenaObjectSnapshot {
        private final ScanResult scanResult;
        private final AngleBracket angleBracket;
        private final Distance maxDistance;

        public ScanSnapshot(ScanResult scanResult, AngleBracket angleBracket, Distance maxDistance) {
            this.scanResult = scanResult;
            this.angleBracket = angleBracket;
            this.maxDistance = maxDistance;
        }

        public void paint(Graphics2D g2d) {
            final Color baseColor = scanResult.successful() ? Color.red : Color.white;
            g2d.setPaint(baseColor);
            final Shape shape = angleBracket.toShape(positionVector.getX(), positionVector.getY(), maxDistance);
            g2d.draw(shape);
            final Composite composite = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
            g2d.fill(shape);
            g2d.setComposite(composite);
        }
    }
}
