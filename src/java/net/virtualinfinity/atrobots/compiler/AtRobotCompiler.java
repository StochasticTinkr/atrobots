package net.virtualinfinity.atrobots.compiler;

import java.io.*;

/**
 * Compiler which will compile source-code.
 *
 * @author Daniel Pitts
 */
public class AtRobotCompiler {

    /**
     * Compile the specific file.
     *
     * @param sourceFile the source file.
     * @return a CompilerOutput object.
     * @throws IOException if there is a problem reading from the file.
     */
    public AtRobotCompilerOutput compile(File sourceFile) throws IOException {
        try (InputStream in = new FileInputStream(sourceFile)) {
            return compile(in);
        }
    }

    /**
     * Compile the source read from the stream.
     *
     * @param in the stream.
     * @return a CompilerOutput object.
     * @throws IOException if there is a problem reading from the stream.
     */
    public AtRobotCompilerOutput compile(InputStream in) throws IOException {
        try (Reader reader = new InputStreamReader(in)) {
            return compile(reader);
        }
    }

    /**
     * Compile the source read from the given reader.
     *
     * @param in the reader
     * @return a CompilerOutput object.
     * @throws IOException if there is a problem reading from the reader.
     */
    public AtRobotCompilerOutput compile(Reader in) throws IOException {
        if (in instanceof LineNumberReader) {
            return compile((LineNumberReader) in);
        }
        try (LineNumberReader reader = new LineNumberReader(in)) {
            return compile(reader);
        }
    }

    /**
     * Compile the source read from the given reader.
     *
     * @param reader the reader
     * @return a CompilerOutput object.
     * @throws IOException if there is a problem reading from the reader.
     */
    public AtRobotCompilerOutput compile(LineNumberReader reader) throws IOException {
        return new LineNumberReaderCompiler().compile(reader);
    }

}
