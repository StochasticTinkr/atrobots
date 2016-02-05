package net.virtualinfinity.atrobots.hardware.mines;


import net.virtualinfinity.atrobots.arena.Arena;
import net.virtualinfinity.atrobots.arena.Position;
import net.virtualinfinity.atrobots.arenaobjects.DamageInflicter;
import net.virtualinfinity.atrobots.ports.PortHandler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Daniel Pitts
 */
public class MineLayer {
    private int mines = 2;
    private final Collection<Mine> laidMines = new ArrayList<>();
    private DamageInflicter owner;
    private Position position;
    private Arena arena;

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
            final Mine mine = new Mine(owner);
            mine.setTriggerRadius(triggerRadius);
            mine.setPosition(position);
            getArena().addMine(mine);
            laidMines.add(mine);
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
        laidMines.forEach(Mine::explode);
    }

    private int countPlacedMines() {
        return (int)laidMines.stream().filter(Mine::isActive).count();
    }

    private Arena getArena() {
        return arena;
    }

    public void setOwner(DamageInflicter owner) {
        this.owner = owner;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
