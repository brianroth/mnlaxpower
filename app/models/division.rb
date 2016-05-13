class Division < ActiveRecord::Base
  validates :name, presence: true, uniqueness: true
  validates :season, :presence => true
  belongs_to :season
  has_many :teams
end
