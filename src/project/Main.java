package project;

import java.util.ArrayList;
import java.sql.Connection;
import java.util.Scanner;

public class Main {
	
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = Database.connect();
            connection.close();
        } catch (Exception e) {
            System.out.println("Database connection failed.");
            System.out.println(e.getMessage());
        }
        
        ArrayList<Team> teams = TeamDAO.loadTeams();
        PlayerDAO.loadPlayers(teams);
        ArrayList<Game> games = GameDAO.loadGames(teams);
        
        int choice;

        do {
            System.out.println("\nBASKETBALL LEAGUE MANAGER");
            System.out.println("1. Add Team");
            System.out.println("2. Add Player");
            System.out.println("3. View Teams");
            System.out.println("4. Schedule Game");
            System.out.println("5. Enter Game Result");
            System.out.println("6. View Standings");
            System.out.println("7. Exit");

            System.out.print("Choose an option: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            if (choice == 1) {

                System.out.print("Enter team name: ");
                String teamName = capitalizeWords(scanner.nextLine());

                boolean teamExists = false;

                for (Team team : teams) {
                    if (team.getName().equalsIgnoreCase(teamName)) {
                        teamExists = true;
                        break;
                    }
                }

                if (teamName.isEmpty()) {
                    System.out.println("Team name cannot be empty.");

                } else if (teamExists) {
                    System.out.println("That team already exists.");

                } else {

                	int teamId = TeamDAO.addTeam(teamName);

                	if (teamId != -1) {

                	    Team newTeam = new Team(teamId, teamName, 0, 0);
                	    teams.add(newTeam);

                	    System.out.println("Team added successfully.");

                	} else {
                	    System.out.println("Team was not added.");
                	}
                }
            } else if (choice == 2) {

                if (teams.isEmpty()) {

                    System.out.println("No teams available. Add a team first.");

                } else {

                    System.out.print("Enter player name: ");
                    String playerName = capitalizeWords(scanner.nextLine());

                    int playerAge;

                    do {
                        System.out.print("Enter player age: ");
                        try {
                            playerAge = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            playerAge = -1;
                        }

                        if (playerAge <= 0) {
                            System.out.println("Age must be greater than 0.");
                        }

                    } while (playerAge <= 0);

                    System.out.print("Enter player position: ");
                    String playerPosition = capitalizeWords(scanner.nextLine());

                    System.out.println("\nAvailable Teams:");

                    for (int i = 0; i < teams.size(); i++) {
                        System.out.println(
                                (i + 1) + ". " + teams.get(i).getName()
                        );
                    }

                    System.out.print("Choose a team: ");

                    int teamChoice;

                    try {
                        teamChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        teamChoice = -1;
                    }
                    
                    if (teamChoice < 1 || teamChoice > teams.size()) {
                        System.out.println(
                                "Invalid team choice. Please select one of the listed teams."
                        );
                    }

                    if (teamChoice >= 1 && teamChoice <= teams.size()) {

                        Player newPlayer =
                                new Player(
                                        playerName,
                                        playerAge,
                                        playerPosition
                                );

                        Team selectedTeam =
                                teams.get(teamChoice - 1);

                        boolean addedToDatabase =
                                PlayerDAO.addPlayer(
                                        newPlayer,
                                        selectedTeam.getTeamId()
                                );

                        if (addedToDatabase) {

                            selectedTeam.addPlayer(newPlayer);

                            System.out.println(
                                    "Player added successfully."
                            );

                        } else {

                            System.out.println(
                                    "Player could not be added. "
                                    + "A player with that name may already "
                                    + "exist on this team."
                            );
                        }

                    } else {

                        System.out.println("Invalid team choice.");
                    }
                }
            } else if (choice == 3) {

                if (teams.isEmpty()) {
                    System.out.println("No teams available.");
                } else {

                    for (Team team : teams) {
                        team.displayRoster();
                    }
                }

            } else if (choice == 4) {

                if (teams.size() < 2) {
                    System.out.println("You need at least 2 teams to schedule a game.");
                } else {

                    System.out.println("\nAvailable Teams:");

                    for (int i = 0; i < teams.size(); i++) {
                        System.out.println((i + 1) + ". " + teams.get(i).getName());
                    }

                    System.out.print("Choose home team: ");
                    int homeChoice = scanner.nextInt();

                    System.out.print("Choose away team: ");
                    int awayChoice = scanner.nextInt();
                    scanner.nextLine();
                    
                    if (homeChoice < 1 || homeChoice > teams.size()
                            || awayChoice < 1 || awayChoice > teams.size()) {

                        System.out.println(
                                "Invalid team selection."
                        );

                    } else if (homeChoice == awayChoice) {

                        System.out.println(
                                "A team cannot play against itself."
                        );

                    } else {
                        
                    if (homeChoice >= 1 && homeChoice <= teams.size()
                            && awayChoice >= 1 && awayChoice <= teams.size()
                            && homeChoice != awayChoice) {

                        Team homeTeam = teams.get(homeChoice - 1);
                        Team awayTeam = teams.get(awayChoice - 1);

                        boolean gameAlreadyScheduled = false;

                        for (Game game : games) {

                            boolean sameOrder =
                                    game.getHomeTeam() == homeTeam
                                    && game.getAwayTeam() == awayTeam;

                            boolean reverseOrder =
                                    game.getHomeTeam() == awayTeam
                                    && game.getAwayTeam() == homeTeam;

                            if (!game.isCompleted() && (sameOrder || reverseOrder)) {
                                gameAlreadyScheduled = true;
                                break;
                            }
                        }
                        
                        if (gameAlreadyScheduled) {

                            System.out.println(
                                    "A pending game between these teams already exists."
                            );

                        } else {

                            Game newGame = new Game(homeTeam, awayTeam);

                            int gameId = GameDAO.addGame(newGame);

                            if (gameId != -1) {

                                Game savedGame =
                                        new Game(gameId, homeTeam, awayTeam);

                                games.add(savedGame);

                                System.out.println("Game scheduled:");
                                System.out.println(savedGame);

                            } else {

                                System.out.println("Game was not scheduled.");
                            }
                        }
                    }
                }
              }
            } else if (choice == 5) {

                if (games.isEmpty()) {
                    System.out.println("No games have been scheduled.");
                } else {

                    ArrayList<Game> pendingGames = new ArrayList<>();

                    System.out.println("\nScheduled Games:");

                    for (Game game : games) {
                        if (!game.isCompleted()) {
                            pendingGames.add(game);
                        }
                    }

                    if (pendingGames.isEmpty()) {
                        System.out.println("There are no pending games.");
                    } else {

                        for (int i = 0; i < pendingGames.size(); i++) {
                            System.out.println((i + 1) + ". " + pendingGames.get(i));
                        }

                        System.out.print("Choose a game: ");
                        int gameChoice = scanner.nextInt();

                        if (gameChoice >= 1 && gameChoice <= pendingGames.size()) {

                            Game selectedGame = pendingGames.get(gameChoice - 1);

                            int homeScore;
                            int awayScore;

                            do {
                                System.out.print("Enter home team score: ");

                                try {
                                    homeScore = Integer.parseInt(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    homeScore = -1;
                                }

                                System.out.print("Enter away team score: ");

                                try {
                                    awayScore = Integer.parseInt(scanner.nextLine());
                                } catch (NumberFormatException e) {
                                    awayScore = -1;
                                }

                                if (homeScore < 0 || awayScore < 0) {
                                    System.out.println(
                                            "Scores must be non-negative numbers."
                                    );
                                }

                            } while (homeScore < 0 || awayScore < 0);

                            boolean resultSaved =
                                    selectedGame.setResult(homeScore, awayScore);

                            if (resultSaved) {

                                boolean gameUpdated =
                                        GameDAO.updateGameResult(selectedGame);

                                boolean homeTeamUpdated =
                                        TeamDAO.updateRecord(selectedGame.getHomeTeam());

                                boolean awayTeamUpdated =
                                        TeamDAO.updateRecord(selectedGame.getAwayTeam());

                                if (gameUpdated && homeTeamUpdated && awayTeamUpdated) {

                                    System.out.println("Result saved:");
                                    System.out.println(selectedGame);

                                } else {

                                    System.out.println(
                                            "The result was processed in Java, "
                                            + "but there was a problem saving it to the database."
                                    );
                                }

                            } else {

                                System.out.println(
                                        "Basketball games cannot end in a draw. "
                                        + "Please enter the final score after overtime."
                                );
                            }
                            
                        } else {
                            scanner.nextLine();
                            System.out.println("Invalid game choice.");
                        }
                    }
                }

            } else if (choice == 6) {

                if (teams.isEmpty()) {
                    System.out.println("No teams available.");
                } else {

                    ArrayList<Team> standings = new ArrayList<>(teams);

                    standings.sort((team1, team2) ->
                            Integer.compare(team2.getWins(), team1.getWins())
                    );

                    System.out.println("\nLEAGUE STANDINGS");
                    System.out.println("----------------------------");
                    System.out.printf("%-15s %-5s %-5s%n", "Team", "W", "L");

                    for (Team team : standings) {
                        System.out.printf(
                                "%-15s %-5d %-5d%n",
                                team.getName(),
                                team.getWins(),
                                team.getLosses()
                        );
                    }
                }

            } else if (choice == 7) {

                System.out.println("Exiting program...");

            } else {

                System.out.println("Invalid choice.");
            }

        } while (choice != 7);

        scanner.close();
    }
    
    public static String capitalizeWords(String text) {

        text = text.trim().toLowerCase();

        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {

            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1))
                      .append(" ");
            }
        }

        return result.toString().trim();
    }
}