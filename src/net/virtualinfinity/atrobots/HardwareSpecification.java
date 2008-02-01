package net.virtualinfinity.atrobots;

import java.util.Map;

/**
 * @author Daniel Pitts
 */
public class HardwareSpecification {
    private final Map<String, Integer> configs;

    public HardwareSpecification(Map<String, Integer> configs) {
        this.configs = configs;
    }

    public Armor createArmor() {
        return new Armor(chooseFor("armor", 50, 66, 100, 120, 130, 150));
    }

    public MineLayer createMineLayer() {
        return new MineLayer(chooseFor("mines", 2, 4, 6, 10, 16, 24));
    }

    public Radar createRadar() {
        return new Radar();
    }

    public Shield createShield() {
        return new Shield(chooseFor("shield", 1.0, 1.0, 1.0, 2.0 / 3, 1.0 / 2, 1.0 / 3));
    }

    public Sonar createSonar() {
        return new Sonar();
    }

    public Transceiver createTransceiver() {
        return new Transceiver();
    }

    public Transponder createTransponder() {
        return new Transponder();
    }

    public Turret createTurret() {
        return new Turret();
    }

    private Scanner createScanner() {
        return new Scanner(chooseFor("scanner", 250, 350, 500, 700, 1000, 1500));
    }


    private Throttle createThrottle() {
        return new Throttle(chooseFor("engine", 0.5, 0.8, 1.0, 1.12, 1.35, 1.50) *
                chooseFor("armor", 1.33, 1.20, 1.00, 0.85, 0.75, 0.66));
    }

    private <T> T chooseFor(String name, T... values) {
        return values[Math.max(0, Math.min(configs.get(name), values.length))];
    }

    void configureHardwareContext(HardwareContext hardwareContext) {
        hardwareContext.setThrottle(createThrottle());
        hardwareContext.setCoolMultiplier(chooseFor("heatsinks", 0.75, 1.00, 1.125, 1.25, 1.33, 1.50));
        hardwareContext.setArmor(createArmor());
        hardwareContext.setMineLayer(createMineLayer());
        hardwareContext.setRadar(createRadar());
        hardwareContext.setShield(createShield());
        hardwareContext.setSonar(createSonar());
        hardwareContext.setTransceiver(createTransceiver());
        hardwareContext.setTransponder(createTransponder());
        hardwareContext.setTurret(createTurret());
        hardwareContext.setMissileLauncher(new MissileLauncher());
        hardwareContext.setMissileLauncherPower(chooseFor("weapon", .5, .8, 1.0, 1.2, 1.35, 1.5));
        hardwareContext.setScanner(createScanner());
        hardwareContext.setHardwareBus(new HardwareBus());
    }
}
