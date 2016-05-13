class Season < ActiveRecord::Base
  validates :name, presence: true, uniqueness: true
  has_many :divisions
end
