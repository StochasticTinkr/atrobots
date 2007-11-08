package net.virtualinfinity.atrobots;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * @author Daniel Pitts
 */
public class ArenaObjectCollection<T extends ArenaObject> extends AbstractCollection<T> {
    private final Collection<T> alive = new ArrayList<T>();
    private final Collection<T> dead = new ArrayList<T>();
    public Iterator<T> iterator() {
        return alive.iterator();
    }

    public int size() {
        updateDead();
        return alive.size();
    }

    public void updateDead() {
        for (Iterator<T> it = this.iterator(); it.hasNext();) {
            T t = it.next();
            if (t.isDead()) {
                dead.add(t);
                it.remove();
            }
        }
    }

    public Collection<T> getDead() {
        return dead;
    }
}
