class Division < ActiveRecord::Base
  validates :name, presence: true, uniqueness: { scope: :season}
  validates :season, :presence => true
  belongs_to :season
  scope :default, -> { where(default: true) }
  has_many :teams
end
