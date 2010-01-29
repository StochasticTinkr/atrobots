package net.virtualinfinity.atrobots;

import net.virtualinfinity.atrobots.parser.AtRobotLineLexer;
import net.virtualinfinity.atrobots.parser.EntrantLineVisitor;

import java.io.*;

/**
 * Configuration for an entrant.
 *
 * @author Daniel Pitts
 */
public class Compiler {

    public Compiler() {
    }

    public CompilerOutput compile(File sourceFile) throws IOException {
        final InputStream in = new FileInputStream(sourceFile);
        try {
            return compile(in);
        } finally {
            in.close();
        }
    }

    public CompilerOutput compile(InputStream in) throws IOException {
        final Reader reader = new InputStreamReader(in);
        try {
            return compile(reader);
        } finally {
            reader.close();
        }
    }

    public CompilerOutput compile(Reader in) throws IOException {
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

    public CompilerOutput compile(LineNumberReader reader) throws IOException {
        final EntrantLineVisitor entrantLineVisitor = new EntrantLineVisitor();
        new AtRobotLineLexer(reader, entrantLineVisitor).visitAllLines();
        entrantLineVisitor.resolve();
        return entrantLineVisitor.createCompilerOutput();
    }

}
