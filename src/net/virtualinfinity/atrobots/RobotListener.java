package net.virtualinfinity.atrobots;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public interface RobotListener {
    void wonRound(Robot robot);

    void tiedRound(Robot robot);

    void killedRobot(Robot robot);

    void died(Robot robot);
}
