package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.computer.Program;
import net.virtualinfinity.atrobots.parser.AtRobotLineLexer;
import net.virtualinfinity.atrobots.parser.EntrantLineVisitor;
import net.virtualinfinity.atrobots.parser.Errors;

import java.io.*;

/**
 * Configuration for an entrant.
 *
 * @author Daniel Pitts
 */
public class EntrantFactory {
    private HardwareSpecification hardwareSpecification;
    private DebugInfo debugInfo;
    private Program program;
    private String name;

    public EntrantFactory() {
    }

    public EntrantFactory(String name) {
        this.name = name;
    }

    public Errors compile(File sourceFile) throws IOException {
        if (name == null) {
            name = sourceFile.getName();
        }
        final InputStream in = new FileInputStream(sourceFile);
        try {
            return compile(in);
        } finally {
            in.close();
        }
    }

    public Errors compile(InputStream in) throws IOException {
        final Reader reader = new InputStreamReader(in);
        try {
            return compile(reader);
        } finally {
            reader.close();
        }
    }

    public Errors compile(Reader in) throws IOException {
        if (in instanceof LineNumberReader) {
            return compile((LineNumberReader) in);
        }
        final LineNumberReader reader = new LineNumberReader(in);
        try {
            return compile(reader);
        } finally {
            reader.close();
        }
    }

    public Errors compile(LineNumberReader reader) throws IOException {
        final EntrantLineVisitor entrantLineVisitor = new EntrantLineVisitor();
        AtRobotLineLexer lexer = new AtRobotLineLexer(reader, entrantLineVisitor);
        entrantLineVisitor.setLexer(lexer);
        lexer.visitAllLines();
        entrantLineVisitor.resolve();
        program = entrantLineVisitor.createProgram();
        hardwareSpecification = entrantLineVisitor.createHardwareSpecification();
        debugInfo = entrantLineVisitor.getDebugInfo();
//        entrantLineVisitor.getLines();
        return entrantLineVisitor.getErrors();
    }

    public Entrant createEntrant() {
        final Entrant entrant = new Entrant();
        entrant.setProgram(getProgram());
        entrant.setName(getName());
        entrant.setHardwareSpecification(getHardwareSpecification());
        entrant.setDebugInfo(debugInfo);
        return entrant;
    }

    public String getName() {
        return name;
    }

    Program getProgram() {
        return program;
    }

    public HardwareSpecification getHardwareSpecification() {
        return hardwareSpecification;
    }
}
