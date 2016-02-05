package net.virtualinfinity.atrobots.arena;

import net.virtualinfinity.atrobots.ArenaObjectVisitor;
import net.virtualinfinity.atrobots.arenaobjects.ArenaObject;
import net.virtualinfinity.atrobots.arenaobjects.CollidableArenaObject;
import net.virtualinfinity.atrobots.arenaobjects.DamageInflicter;
import net.virtualinfinity.atrobots.measures.Duration;
import net.virtualinfinity.atrobots.radio.RadioDispatcher;

import java.util.*;
import java.util.stream.Stream;

/**
 * The Arena is the virtual world within which the simulation occurs.
 *
 * @author Daniel Pitts
 */
public class Arena {
    private final List<TangibleArenaObject> activeRobots = new LinkedList<>();
    private final List<TangibleArenaObject> allRobots = new LinkedList<>();
    private final List<CollidableArenaObject> collidables = new LinkedList<>();
    private final Collection<ArenaObject> intangibles = new LinkedList<>();
    private final RoundTimer roundTimer = new RoundTimer();

    final Collection<Collection<? extends ArenaObject>> allActiveObjects = new ArrayList<>(
        Arrays.asList(collidables, activeRobots, intangibles)
    );

    final Collection<Collection<? extends ArenaObject>> allFramedObjects = new ArrayList<>(
        Arrays.asList(collidables, intangibles, allRobots)
    );

    final Collection<Collection<? extends CollidableArenaObject>> allCollidable = new ArrayList<>(
        Arrays.asList(collidables, activeRobots)
    );


    private final RadioDispatcher radioDispatcher = new RadioDispatcher();
    private final FrameBuilder frameBuilder;
    private boolean roundOver;

    public Arena() {
        this(null);
    }

    public Arena(FrameBuilder frameBuilder) {
        this.frameBuilder = frameBuilder;
    }


    /**
     * Get the number of robots still active in the arena.
     *
     * @return the number of robots still active in the arena.
     */
    public int countActiveRobots() {
        return activeRobots.size();
    }

    private void connectArena(ArenaObject object) {
        object.setArena(this);
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
     * Simulate a certain amount of time elapsing.
     */
    public void simulate() {
        updateSimulation();
        buildFrame();
        roundTimer.increment(Duration.ONE_CYCLE);
    }

    /**
     * Prepare a snapshot of the current arena state in the {@link FrameBuilder}.
     */
    public void buildFrame() {
        if (frameBuilder != null) {
            frameBuilder.beginFrame(roundOver);
            streamOfAllFramedObjects()
                .map(ArenaObject::getSnapshot)
                .forEachOrdered(frameBuilder::addObject);
            frameBuilder.endFrame();
        }
    }

    private Stream<ArenaObject> streamOfAllFramedObjects() {
        return allFramedObjects
            .stream()
            .flatMap(Collection::stream);
    }

    private void updateSimulation() {
        streamOfAllActiveObjects()
            .forEachOrdered(object -> object.update(Duration.ONE_CYCLE));
        checkCollisions();
        removeDead();
    }

    private Stream<ArenaObject> streamOfAllActiveObjects() {
        return allActiveObjects
            .stream()
            .flatMap(Collection::stream);
    }

    private void removeDead() {
        for (final Iterable<? extends ArenaObject> objectCollection : allActiveObjects) {
            for (final Iterator<? extends ArenaObject> it = objectCollection.iterator(); it.hasNext(); ) {
                if (it.next().isDead()) {
                    it.remove();
                }
            }
        }
    }

    private void checkCollisions() {
        for (final TangibleArenaObject collisionTarget : activeRobots) {
            for (final Iterable<? extends CollidableArenaObject> toCheckAgainst : allCollidable) {
                for (final CollidableArenaObject collidable : toCheckAgainst) {
                    if (collidable == collisionTarget) {
                        break;
                    }
                    collidable.checkCollision(collisionTarget);
                }
            }
        }
    }

    /**
     * Add a robot to the arena at a random location.
     *
     * @param robot the robot to add to this arena.
     */
    public void addRobot(TangibleArenaObject robot) {
        robot.getPosition().copyFrom(Position.random(0.0, 0.0, 1000.0, 1000.0));
        connectArena(robot);
        activeRobots.add(robot);
        allRobots.add(robot);
    }

    public void addCollidable(CollidableArenaObject arenaObject) {
        connectArena(arenaObject);
        collidables.add(arenaObject);
    }

    /**
     * Cause an explosion.
     *
     * @param cause             the robot which gets credit for any damage done.
     * @param explosionFunction the damage explosion function.
     */
    public void explosion(DamageInflicter cause, ExplosionFunction explosionFunction) {
        addIntangible(new Explosion(explosionFunction.getCenter(), explosionFunction.getRadius()));
        activeRobots.forEach(
            robot -> explosionFunction.inflictDamage(cause, robot)
        );
    }

    public void determineWinners() {
        if (noneSurvived()) {
            everyoneTies();
            return;
        }
        if (exactlyOneSurvived()) {
            survivorWins();
            return;
        }
        survivorsTie();
    }

    private void survivorsTie() {
        activeRobots.forEach(TangibleArenaObject::tieRound);
    }

    private void survivorWins() {
        activeRobots.forEach(TangibleArenaObject::winRound);
    }

    private boolean exactlyOneSurvived() {
        return activeRobots.size() == 1;
    }

    private void everyoneTies() {
        allRobots.forEach(TangibleArenaObject::tieRound);
    }

    private boolean noneSurvived() {
        return activeRobots.isEmpty();
    }

    public void endRound() {
        roundOver = true;
        determineWinners();
        buildFrame();
    }


    public boolean isOnlyOneRobotAlive() {
        return countActiveRobots() == 1;
    }

    public void addIntangible(ArenaObject object) {
        if (frameBuilder != null) {
            intangibles.add(object);
        }
    }

    public void visitActiveRobots(ArenaObjectVisitor arenaObjectVisitor) {
        activeRobots.forEach(arenaObject -> arenaObject.accept(arenaObjectVisitor));
    }

    public RoundTimer getRoundTimer() {
        return roundTimer;
    }
}
