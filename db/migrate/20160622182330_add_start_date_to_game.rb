class AddStartDateToGame < ActiveRecord::Migration
  def change
    add_column :games, :start_date, :datetime
    add_column :games, :division_id, :integer
    change_column :games, :date, :string, null: true
    change_column :games, :time, :string, null: true
  end
end
