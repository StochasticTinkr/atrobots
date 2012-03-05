package net.virtualinfinity.atrobots;


/**
 * @author Daniel Pitts
 */
public class MineLayer {
    private int mines = 2;
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
        return getArena().countMinesLayedBy(this);
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
