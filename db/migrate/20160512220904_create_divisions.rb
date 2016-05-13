class CreateDivisions < ActiveRecord::Migration
  def change
    create_table :divisions do |t|
      t.string :name, null: false
      t.references :season, null: false
      t.timestamps null: false
    end
  end
end
