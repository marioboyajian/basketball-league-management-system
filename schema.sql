CREATE TABLE teams (
    team_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    wins INT NOT NULL DEFAULT 0,
    losses INT NOT NULL DEFAULT 0
);

CREATE UNIQUE INDEX unique_team_name_case_insensitive
ON teams (LOWER(name));


CREATE TABLE players (
    player_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    position VARCHAR(50),
    team_id INT NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams(team_id)
);

CREATE UNIQUE INDEX unique_player_per_team
ON players (LOWER(name), team_id);


CREATE TABLE games (
    game_id SERIAL PRIMARY KEY,
    home_team_id INT NOT NULL,
    away_team_id INT NOT NULL,
    home_score INT,
    away_score INT,
    completed BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (home_team_id) REFERENCES teams(team_id),
    FOREIGN KEY (away_team_id) REFERENCES teams(team_id),

    CHECK (home_team_id <> away_team_id)
);


CREATE VIEW team_details AS
SELECT
    name AS "Team Name",
    wins AS "Wins",
    losses AS "Losses"
FROM teams;


CREATE VIEW player_details AS
SELECT
    p.name AS "Player Name",
    p.age AS "Age",
    p.position AS "Position",
    t.name AS "Team"
FROM players p
JOIN teams t
    ON p.team_id = t.team_id;


CREATE VIEW game_details AS
SELECT
    home.name AS "Home Team",
    away.name AS "Away Team",
    g.home_score AS "Home Score",
    g.away_score AS "Away Score",
    g.completed AS "Completed"
FROM games g
JOIN teams home
    ON g.home_team_id = home.team_id
JOIN teams away
    ON g.away_team_id = away.team_id;