package project;

import java.util.ArrayList;

public class Team {

    private String name;
    private ArrayList<Player> players;
    private int wins;
    private int losses;
    private int teamId;

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.wins = 0;
        this.losses = 0;
    }
    
    public Team(int teamId, String name, int wins, int losses) {
        this.teamId = teamId;
        this.name = name;
        this.players = new ArrayList<>();
        this.wins = wins;
        this.losses = losses;
    }

    public String getName() {
        return name;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
    
    public int getTeamId() {
        return teamId;
    }

    public void addWin() {
        wins++;
    }

    public void addLoss() {
        losses++;
    }

    public boolean addPlayer(Player player) {

        for (Player existingPlayer : players) {
            if (existingPlayer.getName().equalsIgnoreCase(player.getName())) {
                return false;
            }
        }

        players.add(player);
        return true;
    }

    public void displayRoster() {

        System.out.println("\nTeam: " + name);

        if (players.isEmpty()) {
            System.out.println("No players registered.");
            return;
        }

        for (Player player : players) {
            System.out.println(player);
        }
    }
}