package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Program;
import net.virtualinfinity.atrobots.parser.AtRobotLineLexer;
import net.virtualinfinity.atrobots.parser.EntrantLineVisitor;
import net.virtualinfinity.atrobots.parser.Errors;

import java.io.*;

/**
 * @author Daniel Pitts
 */
public class EntrantFactory {
    private File sourceFile;
    private HardwareSpecification hardwareSpecification;
    private Program program;
    private String name;

    public EntrantFactory(File sourceFile) {
        this.sourceFile = sourceFile;
        name = sourceFile.getName();
    }

    public EntrantFactory(String name) {
        this.name = name;
    }

    public Errors compile() throws IOException {
        final InputStream in = new FileInputStream(sourceFile);
        try {
            return compile(in);
        } finally {
            in.close();
        }
    }

    public Errors compile(InputStream in) throws IOException {
        final LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
        try {
            return compile(reader);
        } finally {
            reader.close();
        }
    }

    private Errors compile(LineNumberReader reader) throws IOException {
        final EntrantLineVisitor entrantLineVisitor = new EntrantLineVisitor();
        AtRobotLineLexer lexer = new AtRobotLineLexer(reader, entrantLineVisitor);
        entrantLineVisitor.setLexer(lexer);
        lexer.visitAllLines();
        entrantLineVisitor.resolve();
        program = entrantLineVisitor.createProgram();
        hardwareSpecification = entrantLineVisitor.createHardwareSpecification();
        return entrantLineVisitor.getErrors();
    }

    public Entrant createEntrant() {
        final Entrant entrant = new Entrant();
        entrant.setProgram(getProgram());
        entrant.setName(getName());
        entrant.setHardwareSpecification(getHardwareSpecification());
        return entrant;
    }

    public String getName() {
        return name;
    }

    private Program getProgram() {
        return program;
    }

    public HardwareSpecification getHardwareSpecification() {
        return hardwareSpecification;
    }
}
