package net.virtualinfinity.atrobots.config;

import net.virtualinfinity.atrobots.Entrant;
import net.virtualinfinity.atrobots.compiler.CompilerOutput;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public class Entrants {
    private final CompilerOutput compilerOutput;
    private final List<Entrant> entrants;

    public Entrants(CompilerOutput compilerOutput, List<Entrant> entrants) {
        this.compilerOutput = compilerOutput;
        this.entrants = entrants;
    }

    public Collection<? extends Entrant> getEntrants() {
        return Collections.unmodifiableCollection(entrants);
    }
}
