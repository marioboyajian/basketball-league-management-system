package project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameDAO {

    public static int addGame(Game game) {

        String sql =
                "INSERT INTO games (home_team_id, away_team_id, completed) "
                + "VALUES (?, ?, false) RETURNING game_id";

        try (
            Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, game.getHomeTeam().getTeamId());
            statement.setInt(2, game.getAwayTeam().getTeamId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("game_id");
            }

        } catch (SQLException e) {
            System.out.println("Could not schedule game.");
            System.out.println(e.getMessage());
        }

        return -1;
    }

    public static ArrayList<Game> loadGames(ArrayList<Team> teams) {

        ArrayList<Game> games = new ArrayList<>();

        String sql =
                "SELECT game_id, home_team_id, away_team_id, "
                + "home_score, away_score, completed "
                + "FROM games ORDER BY game_id";

        try (
            Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                int gameId = resultSet.getInt("game_id");
                int homeTeamId = resultSet.getInt("home_team_id");
                int awayTeamId = resultSet.getInt("away_team_id");
                boolean completed = resultSet.getBoolean("completed");

                Team homeTeam = null;
                Team awayTeam = null;

                for (Team team : teams) {

                    if (team.getTeamId() == homeTeamId) {
                        homeTeam = team;
                    }

                    if (team.getTeamId() == awayTeamId) {
                        awayTeam = team;
                    }
                }

                if (homeTeam != null && awayTeam != null) {

                    Game game = new Game(gameId, homeTeam, awayTeam);

                    if (completed) {

                        int homeScore = resultSet.getInt("home_score");
                        int awayScore = resultSet.getInt("away_score");

                        game.loadResult(homeScore, awayScore);
                    }

                    games.add(game);
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not load games.");
            System.out.println(e.getMessage());
        }

        return games;
    }
    
    public static boolean updateGameResult(Game game) {

        String sql =
                "UPDATE games "
                + "SET home_score = ?, away_score = ?, completed = true "
                + "WHERE game_id = ?";

        try (
            Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, game.getHomeScore());
            statement.setInt(2, game.getAwayScore());
            statement.setInt(3, game.getGameId());

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            System.out.println("Could not save game result.");
            System.out.println(e.getMessage());

            return false;
        }
    }    
}