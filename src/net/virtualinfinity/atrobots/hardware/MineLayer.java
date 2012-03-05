package net.virtualinfinity.atrobots.hardware;


import net.virtualinfinity.atrobots.ports.PortHandler;
import net.virtualinfinity.atrobots.simulation.arena.Arena;
import net.virtualinfinity.atrobots.simulation.atrobot.Robot;
import net.virtualinfinity.atrobots.simulation.mine.Mine;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Daniel Pitts
 */
public class MineLayer {
    private int mines = 2;
    private Collection<Mine> layedMines = new ArrayList<Mine>();
    private Robot robot;

    public MineLayer(int mines) {
        this.mines = mines;
    }

    public PortHandler getMineBayPort() {
        return new PortHandler() {
            public short read() {
                return (short) countMinesRemaining();
            }

            public void write(short value) {
                layMine((value));
            }
        };
    }

    private void layMine(double triggerRadius) {
        if (hasMines()) {
            final Mine mine = new Mine(this);
            mine.setTriggerRadius(triggerRadius);
            mine.setPosition(robot.getPosition());
            getArena().placeMine(mine);
            layedMines.add(mine);
            mines--;
        }
    }

    private boolean hasMines() {
        return countMinesRemaining() > 0;
    }

    private int countMinesRemaining() {
        return mines;
    }

    public PortHandler getPlacedMinePort() {
        return new PortHandler() {
            public short read() {
                return (short) countPlacedMines();
            }

            public void write(short value) {
                detonateMines();
            }
        };
    }

    private void detonateMines() {

    }

    private int countPlacedMines() {
        int count = 0;
        for (Mine mine : layedMines) {
            if (!mine.isDead()) {
                count++;
            }
        }
        return count;
    }

    private Arena getArena() {
        return robot.getArena();
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public Robot getRobot() {
        return robot;
    }
}
