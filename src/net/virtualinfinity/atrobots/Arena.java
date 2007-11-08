package net.virtualinfinity.atrobots;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 @author Daniel Pitts
 */
public class Arena {
    private final Collection<Robot> robots = new ArrayList<Robot>();
    private final Collection<Mine> mines = new ArrayList<Mine>();
    private final RadioDispatcher radioDispatcher = new RadioDispatcher();

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


    public ScanResult scan(Robot ignore, Position position, AngleBracket angleBracket, Distance maxDistance) {
        Vector vectorToClosest = null;
        Distance closestDistance = maxDistance;
        Robot closest = null;
        for (Robot robot: robots) {
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
}
