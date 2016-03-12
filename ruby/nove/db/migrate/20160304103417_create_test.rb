class CreateTest < ActiveRecord::Migration
    say "init database done"

  def change
    create_table :tests do |t|
      t.string :name
      t.string :test
      t.timestamps null: false
    end
  end
end
