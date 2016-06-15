class Organization < ActiveRecord::Base
  has_many :teams
  validates :name, presence: true, uniqueness: true

  default_scope { order('name') }

  def avg
    if games_count == 0
      0.0
    else
      (wins.to_f/games_count.to_f).round(3)
    end
  end
end
