class CreateTeams < ActiveRecord::Migration
  def change
    create_table :teams do |t|
      t.string :name, null: false
      t.integer :cms_code, null: false
      t.references :division, null: false
      t.timestamps null: false
    end
  end
end
