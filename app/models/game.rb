class Game < ActiveRecord::Base
  validates :cms_code, presence: true
  validates :location, presence: true
  validates :date, presence: true
  validates :time, presence: true
  validates :home_team, presence: true
  validates :home_team_score, numericality: { only_integer: true, greater_than_or_equal_to: 0 }
  validates :away_team, presence: true
  validates :away_team_score, numericality: { only_integer: true, greater_than_or_equal_to: 0 }

  belongs_to :home_team, :class_name => 'Team', :foreign_key => 'home_team_id'
  belongs_to :away_team, :class_name => 'Team', :foreign_key => 'away_team_id'

  default_scope { order('date ASC') }
end
