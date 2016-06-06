# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20160606214035) do

  create_table "divisions", force: :cascade do |t|
    t.string   "name",                       null: false
    t.integer  "season_id",                  null: false
    t.integer  "cms_code",                   null: false
    t.boolean  "default",    default: false
    t.datetime "created_at",                 null: false
    t.datetime "updated_at",                 null: false
  end

  create_table "games", force: :cascade do |t|
    t.integer  "cms_code",        null: false
    t.string   "location",        null: false
    t.string   "date",            null: false
    t.string   "time",            null: false
    t.integer  "home_team_id",    null: false
    t.integer  "home_team_score", null: false
    t.integer  "away_team_id",    null: false
    t.integer  "away_team_score", null: false
    t.string   "commentary"
    t.datetime "created_at",      null: false
    t.datetime "updated_at",      null: false
  end

  create_table "organizations", force: :cascade do |t|
    t.string   "name",                    null: false
    t.datetime "created_at",              null: false
    t.datetime "updated_at",              null: false
    t.integer  "teams_count", default: 0
    t.integer  "games_count", default: 0
    t.integer  "wins",        default: 0
  end

  create_table "seasons", force: :cascade do |t|
    t.string   "name",                       null: false
    t.boolean  "default",    default: false
    t.integer  "cms_code",                   null: false
    t.datetime "created_at",                 null: false
    t.datetime "updated_at",                 null: false
  end

  create_table "teams", force: :cascade do |t|
    t.string   "name",                           null: false
    t.integer  "cms_code",                       null: false
    t.integer  "wins",             default: 0
    t.integer  "ties",             default: 0
    t.integer  "home_games_count", default: 0
    t.integer  "away_games_count", default: 0
    t.float    "rpi",              default: 0.0
    t.integer  "division_id",                    null: false
    t.datetime "created_at",                     null: false
    t.datetime "updated_at",                     null: false
    t.integer  "organization_id"
  end

end
