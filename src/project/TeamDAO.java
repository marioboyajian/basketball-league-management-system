package project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamDAO {

	public static int addTeam(String teamName) {

	    String sql = "INSERT INTO teams (name) VALUES (?) RETURNING team_id";

	    try (
	        Connection connection = Database.connect();
	        PreparedStatement statement = connection.prepareStatement(sql)
	    ) {

	        statement.setString(1, teamName);

	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            return resultSet.getInt("team_id");
	        }

	    } catch (SQLException e) {
	        System.out.println("Could not add team.");
	        System.out.println(e.getMessage());
	    }

	    return -1;
	}

    public static ArrayList<Team> loadTeams() {

        ArrayList<Team> teams = new ArrayList<>();

        String sql =
                "SELECT team_id, name, wins, losses FROM teams ORDER BY team_id";

        try (
            Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                int teamId = resultSet.getInt("team_id");
                String name = resultSet.getString("name");
                int wins = resultSet.getInt("wins");
                int losses = resultSet.getInt("losses");

                Team team = new Team(teamId, name, wins, losses);
                teams.add(team);
            }

        } catch (SQLException e) {

            System.out.println("Could not load teams.");
            System.out.println(e.getMessage());
        }

        return teams;
    }
    
    public static boolean updateRecord(Team team) {

        String sql =
                "UPDATE teams "
                + "SET wins = ?, losses = ? "
                + "WHERE team_id = ?";

        try (
            Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, team.getWins());
            statement.setInt(2, team.getLosses());
            statement.setInt(3, team.getTeamId());

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            System.out.println("Could not update team record.");
            System.out.println(e.getMessage());

            return false;
        }
    }
}