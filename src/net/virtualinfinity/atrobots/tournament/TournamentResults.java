package net.virtualinfinity.atrobots.tournament;

import net.virtualinfinity.atrobots.compiler.RobotFactory;
import net.virtualinfinity.atrobots.game.Game;
import net.virtualinfinity.atrobots.robot.RobotScoreKeeper;

import java.util.*;

/**
 * @author <a href='mailto:daniel.pitts@cbs.com'>Daniel Pitts</a>
 */
public class TournamentResults {
    private final Map<RobotFactory, Score> scores = new IdentityHashMap<RobotFactory, Score>();

    public synchronized void recordScores(Game game, RobotFactory... particpants) {
        final List<GameScore> gameScores = new ArrayList<GameScore>();
        for (RobotFactory robotFactory : particpants) {
            gameScores.add(new GameScore(robotFactory, game.getScoreKeeper(robotFactory)));
        }
        final GameScore winner = Collections.max(gameScores);
        final List<GameScore> winners = new ArrayList<GameScore>();
        for (GameScore gameScore : gameScores) {
            getTournamentScore(winner.participant).addGameScore(gameScore);
            if (gameScore.compareTo(winner) == 0) {
                winners.add(gameScore);
            } else {
                getTournamentScore(gameScore.participant).lostGame();
            }
        }
        if (winners.size() == 1) {
            getTournamentScore(winner.participant).wonGame();
        } else {
            for (GameScore gameScore : winners) {
                getTournamentScore(gameScore.participant).tieGame();
            }
        }
    }

    private Score getTournamentScore(RobotFactory participant) {
        Score score = scores.get(participant);
        if (score == null) {
            score = new Score(participant);
            scores.put(participant, score);
        }
        return score;
    }

    public List<Score> getScores() {
        final List<Score> scoreList = new ArrayList<Score>(scores.values());
        Collections.sort(scoreList, new Comparator<Score>() {
            public int compare(Score o1, Score o2) {
                return o1.totalGameWins > o2.totalGameWins ? -1 : o1.totalGameWins < o2.totalGameWins ? 1 : 0;
            }
        });
        return scoreList;
    }


    public static class Score {
        private final RobotFactory participant;
        private int totalGameWins;
        private int totalGameLoses;
        private int totalRoundWins;
        private int totalGamesTied;

        public Score(RobotFactory participant) {
            this.participant = participant;
        }

        public void addGameScore(GameScore gameScore) {
            totalRoundWins += gameScore.getTotalWins();
        }

        public void wonGame() {
            ++totalGameWins;
        }

        public void tieGame() {
            ++totalGamesTied;
        }

        public void lostGame() {
            ++totalGameLoses;
        }

        public int getRoundWins() {
            return totalRoundWins;
        }

        public int getTies() {
            return totalGamesTied;
        }

        public int getTotalWins() {
            return totalGameWins;
        }

        String getName() {
            return participant.getName();
        }

    }

    private class GameScore implements Comparable<GameScore> {
        private final RobotFactory participant;
        private final RobotScoreKeeper scoreKeeper;

        public GameScore(RobotFactory participant, RobotScoreKeeper scoreKeeper) {
            this.participant = participant;
            this.scoreKeeper = scoreKeeper;
        }

        public int compareTo(GameScore o) {
            return compare(scoreKeeper.getTotalWins(), o.scoreKeeper.getTotalWins(), 16) +
                    compare(scoreKeeper.getTotalTies(), o.scoreKeeper.getTotalTies(), 8) +
                    compare(scoreKeeper.getTotalDeaths(), o.scoreKeeper.getTotalDeaths(), -4) +
                    compare(scoreKeeper.getTotalKills(), o.scoreKeeper.getTotalKills(), 2) +
                    compare(scoreKeeper.getTotalDamageInflicted(), o.scoreKeeper.getTotalDamageInflicted(), 1);

        }

        private int compare(int left, int right, int weight) {
            return left < right ? -weight : left > right ? weight : 0;
        }

        private int compare(double left, double right, int weight) {
            return Double.compare(left, right) * weight;
        }

        public int getTotalWins() {
            return scoreKeeper.getTotalWins();
        }

    }
}
