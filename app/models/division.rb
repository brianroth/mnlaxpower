class Division < ActiveRecord::Base
  validates :name, presence: true, uniqueness: { scope: :season}
  validates :cms_code, presence: true, uniqueness: { scope: :season}

  validates :season, :presence => true
  belongs_to :season
  scope :default, -> { where(default: true) }
  default_scope { order('name DESC') }
  has_many :teams
  has_many :games
end
