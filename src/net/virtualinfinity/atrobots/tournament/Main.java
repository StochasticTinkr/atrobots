package net.virtualinfinity.atrobots.tournament;

import net.virtualinfinity.atrobots.compiler.AtRobotCompiler;
import net.virtualinfinity.atrobots.compiler.AtRobotCompilerOutput;
import net.virtualinfinity.atrobots.compiler.RobotFactory;
import net.virtualinfinity.atrobots.network.Server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO: JavaDoc
 *
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public class Main {


    public static void main(String[] args) throws InterruptedException, IOException {
        final List<String> realArgs = new ArrayList<String>(Arrays.asList(args));
        final boolean runServer = realArgs.remove("--server");

        final List<RobotFactory> competitors = new ArrayList<RobotFactory>();
        final AtRobotCompiler compiler = new AtRobotCompiler();
        for (String arg : realArgs) {
            try {
                final AtRobotCompilerOutput compile = compiler.compile(getRobotFile(arg));
                competitors.add(compile.createRobotFactory(arg));
            } catch (Exception e) {
                System.out.println(arg + " failed to compile, disqualified. ");
                e.printStackTrace();
            }
        }
        final Tournament tournament;
        if (!runServer) {
            tournament = new Tournament();
        } else {
            tournament = new Tournament(25);
            final Server server = new Server(new ServerSocket(2001));
            server.setBuffer(tournament.getFrameBuffer());
            final Thread thread = new Thread(server);
            thread.setDaemon(true);
            thread.start();
        }
        tournament.setRoundsPerPairing(10);
        tournament.setCompetitors(competitors);
        final TournamentResults results = tournament.run();

        System.out.println("Name:\tGames Won\tGames tied\tRounds won");
        for (TournamentResults.Score score : results.getScores()) {
            System.out.println(score.getName() + ":\t" + score.getTotalWins() + "\t" + score.getTies() + "\t" + score.getRoundWins());
        }
    }

    private static File getRobotFile(String arg) {
        return new File(arg);
    }
}
