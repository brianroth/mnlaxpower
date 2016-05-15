class CreateDivisions < ActiveRecord::Migration
  def change
    create_table :divisions do |t|
      t.string :name, null: false
      t.references :season, null: false
      t.integer :cms_code, null: false
      t.boolean :default, default: false
      t.timestamps null: false
    end
  end
end
