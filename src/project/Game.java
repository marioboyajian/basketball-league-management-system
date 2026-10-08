package project;

public class Game {

    private Team homeTeam;
    private Team awayTeam;
    private int homeScore;
    private int awayScore;
    private boolean completed;
    private int gameId;

    public Game(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.completed = false;
    }
    
    public Game(int gameId, Team homeTeam, Team awayTeam) {
        this.gameId = gameId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.completed = false;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public boolean isCompleted() {
        return completed;
    }
    
    public int getGameId() {
        return gameId;
    }
    
    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }
    
    public void loadResult(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.completed = true;
    }
    
    public boolean setResult(int homeScore, int awayScore) {

        if (homeScore == awayScore) {
            return false;
        }

        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.completed = true;

        if (homeScore > awayScore) {
            homeTeam.addWin();
            awayTeam.addLoss();
        } else {
            awayTeam.addWin();
            homeTeam.addLoss();
        }

        return true;
    }

    @Override
    public String toString() {

        if (completed) {
            return homeTeam.getName() + " "
                    + homeScore + " - "
                    + awayScore + " "
                    + awayTeam.getName();
        }

        return homeTeam.getName()
                + " vs "
                + awayTeam.getName();
    }
}