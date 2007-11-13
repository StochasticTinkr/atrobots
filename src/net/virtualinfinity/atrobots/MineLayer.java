package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class MineLayer {
    private Arena arena;
    private int mines = 2;
    private Robot robot;

    public PortHandler getMineBayPort() {
        return new PortHandler() {
            public short read() {
                return (short) countMinesRemaining();
            }

            public void write(short value) {
                layMine(Distance.fromMeters(value));
            }
        };
    }

    private void layMine(Distance triggerRadius) {
        if (hasMines()) {
            final Mine mine = new Mine(this, robot);
            mine.setTriggerRadius(triggerRadius);
            mine.setPosition(robot.getPosition());
            arena.placeMine(mine);
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
        return arena.countMinesLayedBy(this);
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
