package net.virtualinfinity.atrobots.debugger;

import net.virtualinfinity.atrobots.computer.Computer;

import java.io.IOException;

/**
 * TODO: Describe this class.
 *
 * @author Daniel Pitts
 */
public class DebugConsole {
    private final Debugger debugger;
    private final Console console;

    private DebugConsole(Debugger debugger, Console console) {
        this.console = console;
        this.debugger = debugger;
    }

    public static DebugConsole create(Console console) {
        return create(console, new Debugger());
    }

    public static DebugConsole create(Console console, Debugger debugger) {
        return new DebugConsole(debugger, console).start();
    }

    private void inputLoop() throws IOException {
        for (String line = readline(); line != null; line = readline()) {
            handleLine(line);
        }
    }

    private void handleLine(String line) {
        try {
            handleInput(line);
        } catch (Exception e) {
            console.handleException(e);
            e.printStackTrace();
        }
    }

    private void handleInput(String line) throws InterruptedException {
        if (line.trim().length() == 0) {
            debugger.step();
            return;
        }
        final String[] tokens = line.split("\\s++");
        if (tokens[0].equalsIgnoreCase("pause")) {
            if (tokens.length > 1) {
                debugger.pause(Integer.parseInt(tokens[1]));
            } else {
                debugger.pause();
            }
        } else if (tokens[0].equalsIgnoreCase("go")) {
            debugger.go();
        } else if (tokens[0].equalsIgnoreCase("step")) {
            debugger.step();
        } else if (tokens[0].equalsIgnoreCase("break")) {
            if (tokens.length > 1) {
                debugger.setBreakpoint(Integer.parseInt(tokens[1]));
            } else {
                debugger.setBreakpoint();
            }
        } else if (tokens[0].equalsIgnoreCase("unbreak")) {
            if (tokens.length > 1) {
                debugger.clearBreakpoint(Integer.parseInt(tokens[1]));
            } else {
                debugger.clearBreakpoint();
            }
        } else if (tokens[0].equalsIgnoreCase("entrant")) {
            if (tokens.length > 1) {
                debugger.setDefaultEntrant(Integer.parseInt(tokens[1]));
            } else {
                debugger.resetDefaultEntrant();
            }
        } else if (tokens[0].equalsIgnoreCase("unbreak")) {
        } else {
            println("Unknown command.");
        }
    }

    private void printState(Computer computer) {
        println("[" + computer.getFlags() + "] " + computer.getRegisters());
        println(computer.getInstructionString());
    }


    private DebugConsole start() {
        debugger.setBreakpointHandler(new MyBreakpointHandler());
        final Thread thread = new Thread("Debugger") {
            public void run() {
                try {
                    inputLoop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        return DebugConsole.this;
    }

    public Debugger getDebugger() {
        return debugger;
    }

    private class MyBreakpointHandler implements BreakpointHandler {
        public void handleBreakpoint(Computer computer) {
            printState(computer);
        }
    }

    public void println(Object o) {
        console.println(o);
    }

    public String readline() throws IOException {
        return console.readline();
    }
}
