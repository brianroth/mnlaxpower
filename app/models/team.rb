class Team < ActiveRecord::Base
  validates :name, presence: true, uniqueness: { scope: :division}
  validates :cms_code, presence: true, uniqueness: true

  belongs_to :division
  has_many :home_games, class_name: 'Game', foreign_key: :home_team_id, counter_cache: true
  has_many :away_games, class_name: 'Game', foreign_key: :away_team_id, counter_cache: true

  def games
    Game.where("home_team_id = ? OR away_team_id = ?", id, id).order(date: :desc)
  end

  def games_count
    home_games_count + away_games_count
  end

  def points
    3*wins.to_i + ties.to_i
  end
end
