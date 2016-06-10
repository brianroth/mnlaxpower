class AddLossesToTeams < ActiveRecord::Migration
  def change
    add_column :teams, :losses, :integer, default: 0
  end
end
