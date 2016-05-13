class CreateGames < ActiveRecord::Migration
  def change
    create_table :games do |t|
      t.string :location, null: false
      t.datetime :date, null: false
      t.integer :home_team_id, null: false
      t.integer :home_team_score, null: false
      t.integer :away_team_id, null: false
      t.integer :away_team_score, null: false
      t.timestamps null: false
    end
  end
end
