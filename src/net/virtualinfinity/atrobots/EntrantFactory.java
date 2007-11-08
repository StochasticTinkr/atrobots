package net.virtualinfinity.atrobots;

import java.io.*;

/**
 * @author Daniel Pitts
 */
public class EntrantFactory {
    private File sourceFile;
    private HardwareSpecification hardwareSpecification;
    private Program program;

    public EntrantFactory(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public Errors compile() throws IOException {
        final InputStream in = new FileInputStream(sourceFile);
        try {
            return compile(in);
        } finally {
            in.close();
        }
    }

    private Errors compile(InputStream in) throws IOException {
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
        return sourceFile.getName();
    }

    private Program getProgram() {
        return program;
    }

    public HardwareSpecification getHardwareSpecification() {
        return hardwareSpecification;
    }
}
