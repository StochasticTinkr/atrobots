package net.virtualinfinity.atrobots.measures;

/**
 * @author Daniel Pitts
 */
public final class Area {
    private final double squareMeters;

    private Area(double squareMeters) {
        this.squareMeters = squareMeters;
    }

    public double getSquareMeters() {
        return squareMeters;
    }

    public static Area fromSquareMeters(double squareMeters) {
        return new Area(squareMeters);
    }

    public Area plus(Area area) {
        return fromSquareMeters(getSquareMeters() + area.getSquareMeters());
    }

    public Distance squareRoot() {
        return Distance.fromMeters(Math.sqrt(getSquareMeters()));
    }

    public double divide(Area area) {
        return getSquareMeters() / area.getSquareMeters();
    }
}
