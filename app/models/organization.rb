class Organization < ActiveRecord::Base
  has_many :teams
  validates :name, presence: true, uniqueness: true
end
