class Team < ApplicationRecord
  validates :name, presence: true, uniqueness: { scope: :division}
  validates :cms_code, presence: true, uniqueness: true

  belongs_to :organization
  belongs_to :division
  has_many :home_games, class_name: 'Game', foreign_key: :home_team_id
  has_many :away_games, class_name: 'Game', foreign_key: :away_team_id

  def games
    Game.where("home_team_id = ? OR away_team_id = ?", id, id).order(date: :desc)
  end

  def games_count
    home_games_count + away_games_count
  end

  def points
    3*wins.to_i + ties.to_i
  end

  def wp
    games_played = self.wins + self.losses + self.ties
    games_won = self.wins

    if games_played == 0
      0.0
    else
      (1.0 * games_won) / games_played
    end
  end

  def owp
    cumulative_owp = 0.0

    self.games.played.each do |game|
      opponent = game.opponent(self)
      games_played = 0
      games_won = 0

      opponent.games.each do |opponent_game|
        opponent_opponent = opponent_game.opponent(opponent)

        if opponent_opponent != self
          games_played += 1
          games_won += 1 if opponent_game.winner?(opponent)
        end
      end

      if games_played != 0
        cumulative_owp += (1.0 * games_won) / games_played
      end
    end

    if self.games.played.count == 0
      0.0
    else
      (1.0 * cumulative_owp) / self.games.played.count
    end
  end

  def oowp
    games_played = 0
    games_won = 0

    self.games.played.each do |game|
      opponent = game.opponent(self)

      opponent.games.played.each do |opponent_game|
        opponent_opponent = opponent_game.opponent(opponent)

        if opponent_opponent != self
          games_played += (opponent_opponent.wins + opponent_opponent.losses + opponent_opponent.ties)
          games_won += opponent_opponent.wins
        end
      end
    end

    if games_played == 0
      0
    else
      (1.0 * games_won) / games_played
    end
  end
end
