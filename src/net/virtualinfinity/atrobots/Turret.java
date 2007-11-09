package net.virtualinfinity.atrobots;

/**
 * @author Daniel Pitts
 */
public class Turret implements Resetable {
    private final Heading heading = new Heading();
    private boolean keepshift;
    private Scanner scanner;
    private MissileLauncher missileLauncher;

    public void setKeepshift(boolean keepshift) {
        this.keepshift = keepshift;
    }

    public Heading getHeading() {
        return heading;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public MissileLauncher getMissileLauncher() {
        return missileLauncher;
    }

    public void setMissileLauncher(MissileLauncher missileLauncher) {
        this.missileLauncher = missileLauncher;
    }

    public void reset() {
        setKeepshift(false);

    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}
