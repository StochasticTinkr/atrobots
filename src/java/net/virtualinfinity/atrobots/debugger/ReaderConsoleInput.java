package net.virtualinfinity.atrobots.debugger;

import java.io.*;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public class ReaderConsoleInput implements ConsoleInput {
    private final BufferedReader reader;

    public ReaderConsoleInput(InputStream in) {
        this(new InputStreamReader(in));
    }

    public ReaderConsoleInput(Reader reader) {
        this(new BufferedReader(reader));
    }

    public ReaderConsoleInput(BufferedReader reader) {
        this.reader = reader;
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }
}
