package project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerDAO {

    public static boolean addPlayer(Player player, int teamId) {

        String sql =
                "INSERT INTO players (name, age, position, team_id) VALUES (?, ?, ?, ?)";

        try (
            Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, player.getName());
            statement.setInt(2, player.getAge());
            statement.setString(3, player.getPosition());
            statement.setInt(4, teamId);

            statement.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Could not add player.");
            System.out.println(e.getMessage());

            return false;
        }
    }

    public static void loadPlayers(ArrayList<Team> teams) {

        String sql =
                "SELECT player_id, name, age, position, team_id FROM players ORDER BY player_id";

        try (
            Connection connection = Database.connect();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String position = resultSet.getString("position");
                int teamId = resultSet.getInt("team_id");

                Player player = new Player(name, age, position);

                for (Team team : teams) {

                    if (team.getTeamId() == teamId) {
                        team.addPlayer(player);
                        break;
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println("Could not load players.");
            System.out.println(e.getMessage());
        }
    }
}