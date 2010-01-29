package net.virtualinfinity.atrobots.config;

import net.virtualinfinity.atrobots.Compiler;
import net.virtualinfinity.atrobots.CompilerOutput;
import net.virtualinfinity.atrobots.Entrant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Describes a source for an entrant.
 *
 * @author Daniel Pitts
 */
public abstract class EntrantSource {
    private boolean debug;
    private int numberOfEntrants = 1;
    private String name;
    private Compiler compiler = new Compiler();

    public final boolean isDebug() {
        return debug;
    }

    public final void setDebug(boolean debug) {
        this.debug = debug;
    }

    public final int getNumberOfEntrants() {
        return numberOfEntrants;
    }

    public final void setNumberOfEntrants(int numberOfEntrants) {
        this.numberOfEntrants = numberOfEntrants;
    }

    public final String getName() {
        return name != null ? name : getDefaultName();
    }

    protected String getDefaultName() {
        return null;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final Entrants createEntrants() throws IOException {
        CompilerOutput factory = compile(compiler);
        final List<Entrant> entrants = new ArrayList<Entrant>();
        for (int i = 0; i < numberOfEntrants; ++i) {
            entrants.add(factory.createEntrant(getName()));
        }
        if (debug && !entrants.isEmpty()) {
            entrants.get(0).setDebug(true);
        }
        return new Entrants(factory, entrants);
    }

    protected abstract CompilerOutput compile(Compiler compiler) throws IOException;

    public Compiler getCompiler() {
        return compiler;
    }

    public void setCompiler(Compiler compiler) {
        this.compiler = compiler;
    }

    @Override
    public String toString() {
        return getName();
    }
}
