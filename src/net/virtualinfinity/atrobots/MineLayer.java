package net.virtualinfinity.atrobots;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Pitts
 */
public class MineLayer {
    private Arena arena;
    private final List<Mine> unlayedMines = new ArrayList<Mine>();
    private Position position;

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
            final Mine mine = popUnlayedMine();
            mine.setTriggerRadius(triggerRadius);
            mine.setPosition(position);
            arena.placeMine(mine);
        }
    }

    private Mine popUnlayedMine() {
        final Mine mine = unlayedMines.remove(unlayedMines.size() - 1);
        return mine;
    }

    private boolean hasMines() {
        return countMinesRemaining() > 0;
    }

    private int countMinesRemaining() {
        return unlayedMines.size();
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
