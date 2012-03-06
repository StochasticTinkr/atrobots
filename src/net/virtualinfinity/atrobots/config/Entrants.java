package net.virtualinfinity.atrobots.config;

import net.virtualinfinity.atrobots.RobotFactory;
import net.virtualinfinity.atrobots.compiler.AtRobotCompilerOutput;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public class Entrants {
    private final AtRobotCompilerOutput compilerOutput;
    private final List<RobotFactory> entrants;

    public Entrants(AtRobotCompilerOutput compilerOutput, List<RobotFactory> entrants) {
        this.compilerOutput = compilerOutput;
        this.entrants = entrants;
    }

    public Collection<? extends RobotFactory> getEntrants() {
        return Collections.unmodifiableCollection(entrants);
    }
}
