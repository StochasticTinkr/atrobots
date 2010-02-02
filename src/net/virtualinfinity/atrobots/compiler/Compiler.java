package net.virtualinfinity.atrobots.compiler;

import java.io.*;

/**
 * Compiler which will compile source-code.
 *
 * @author Daniel Pitts
 */
public class Compiler {

    public Compiler() {
    }

    /**
     * Compile the specific file.
     *
     * @param sourceFile the source file.
     * @return a CompilerOutput object.
     * @throws IOException if there is a problem reading from the file.
     */
    public CompilerOutput compile(File sourceFile) throws IOException {
        final InputStream in = new FileInputStream(sourceFile);
        try {
            return compile(in);
        } finally {
            in.close();
        }
    }

    /**
     * Compile the source read from the stream.
     *
     * @param in the stream.
     * @return a CompilerOutput object.
     * @throws IOException if there is a problem reading from the stream.
     */
    public CompilerOutput compile(InputStream in) throws IOException {
        final Reader reader = new InputStreamReader(in);
        try {
            return compile(reader);
        } finally {
            reader.close();
        }
    }

    /**
     * Compile the source read from the given reader.
     *
     * @param in the reader
     * @return a CompilerOutput object.
     * @throws IOException if there is a problem reading from the reader.
     */
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

    /**
     * Compile the source read from the given reader.
     *
     * @param reader the reader
     * @return a CompilerOutput object.
     * @throws IOException if there is a problem reading from the reader.
     */
    public CompilerOutput compile(LineNumberReader reader) throws IOException {
        return new LineNumberReaderCompiler().compile(reader);
    }

}
