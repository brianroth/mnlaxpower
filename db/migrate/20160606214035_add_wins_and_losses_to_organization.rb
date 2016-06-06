class AddWinsAndLossesToOrganization < ActiveRecord::Migration
  def change
    add_column :organizations, :teams_count, :integer, default: 0
    add_column :organizations, :games_count, :integer, default: 0
    add_column :organizations, :wins, :integer, default: 0
  end
end
