class CreateGames < ActiveRecord::Migration
  def change
    create_table :games do |t|
      t.integer :cms_code, null: false
      t.string :location, null: false
      t.datetime :date, null: false
      t.integer :home_team_id, null: false
      t.integer :home_team_score, null: false
      t.integer :away_team_id, null: false
      t.integer :away_team_score, null: false
      t.string :commentary
      t.timestamps null: false
    end
  end
end
