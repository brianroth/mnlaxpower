class Season < ApplicationRecord
  validates :name, presence: true, uniqueness: true
  validates :cms_code, presence: true, uniqueness: true

  scope :default, -> { where(default: true) }
  default_scope { order('name DESC') }
  has_many :divisions
end
