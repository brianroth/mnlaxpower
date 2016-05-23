class CreateTeams < ActiveRecord::Migration
  def change
    create_table :teams do |t|
      t.string :name, null: false
      t.integer :cms_code, null: false
      t.integer :wins, default: 0
      t.integer :ties, default: 0
      t.integer :home_games_count, default: 0
      t.integer :away_games_count, default: 0
      t.float :rpi, default: 0
      t.references :division, null: false
      t.timestamps null: false
    end
  end
end
