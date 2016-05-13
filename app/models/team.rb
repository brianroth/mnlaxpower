class Team < ActiveRecord::Base
  validates :name, presence: true, uniqueness: { scope: :division}
  validates :cms_code, presence: true, uniqueness: true

  belongs_to :division
  has_many :home_games, class_name: 'Game', foreign_key: :home_team_id
  has_many :away_games, class_name: 'Game', foreign_key: :away_team_id

  def games
    Game.where("home_team_id = ? OR away_team_id = ?", id, id)
  end

  def wins
    Game.where("(home_team_id = ? and home_team_score > away_team_score) or (away_team_id = ? and away_team_score > home_team_score)", id, id).order(date: :desc)
  end

  def ties
    Game.where("(home_team_id = ? or away_team_id = ?) and home_team_score = away_team_score", id, id)
  end

  def points
    3*wins.count+ties.count
  end

  def rpi
    
  end
end
