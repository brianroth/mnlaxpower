class Season < ActiveRecord::Base
  validates :name, presence: true, uniqueness: true
  scope :default, -> { where(default: true) }
  has_many :divisions
end
