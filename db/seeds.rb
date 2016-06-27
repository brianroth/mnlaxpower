Organization.create(name: 'ACYLA')
Organization.create(name: 'Andover')
Organization.create(name: 'Anoka')
Organization.create(name: 'Apple Valley')
Organization.create(name: 'Becker-Big Lake')
Organization.create(name: 'Blaine')
Organization.create(name: 'Blake School')
Organization.create(name: 'Bloomington')
Organization.create(name: 'Breck')
Organization.create(name: 'Buffalo')
Organization.create(name: 'Burnsville')
Organization.create(name: 'Centennial')
Organization.create(name: 'Champlin Park')
Organization.create(name: 'Chaska-Chanhassen')
Organization.create(name: 'Chisago')
Organization.create(name: 'Coon Rapids')
Organization.create(name: 'Cottage')
Organization.create(name: 'Delano')
Organization.create(name: 'Eagan')
Organization.create(name: 'Eastview')
Organization.create(name: 'Eden Prarie')
Organization.create(name: 'Edina')
Organization.create(name: 'Elk River')
Organization.create(name: 'Farmington')
Organization.create(name: 'Forest')
Organization.create(name: 'Hastings')
Organization.create(name: 'Hopkins')
Organization.create(name: 'Hudson')
Organization.create(name: 'Inver Grove Heights')
Organization.create(name: 'Lakeville')
Organization.create(name: 'Mahtomedi')
Organization.create(name: 'Maple Grove')
Organization.create(name: 'Mendota Heights')
Organization.create(name: 'Minneapolis')
Organization.create(name: 'Minnetonka')
Organization.create(name: 'Moundsview')
Organization.create(name: 'New Brighton')
Organization.create(name: 'Northfield')
Organization.create(name: 'North West Lacrosse Club')
Organization.create(name: 'Oakdale')
Organization.create(name: 'Orono')
Organization.create(name: 'Osseo')
Organization.create(name: 'Prairie Island')
Organization.create(name: 'Prior Lake')
Organization.create(name: 'RedWing')
Organization.create(name: 'Rochester')
Organization.create(name: 'Rogers')
Organization.create(name: 'Rosemount')
Organization.create(name: 'Roseville')
Organization.create(name: 'Saint Anthony Village')
Organization.create(name: 'Shakopee')
Organization.create(name: 'Spring Lake Park')
Organization.create(name: 'St. Cloud')
Organization.create(name: 'Stillwater')
Organization.create(name: 'St. Louis Park')
Organization.create(name: 'St. Michael-Albertville')
Organization.create(name: 'St. Paul')
Organization.create(name: 'Waconia')
Organization.create(name: 'Wayzata')
Organization.create(name: 'Westonka')
Organization.create(name: 'White Bear Lake')
Organization.create(name: 'Woodbury')

season = Season.default.first

division = season.divisions.find_by_cms_code(18301)

eagan = division.teams.find_by_cms_code(428877)
minnetonka = division.teams.find_by_cms_code(428873)
stillwater = division.teams.find_by_cms_code(428589)
woodbury = division.teams.find_by_cms_code(428876)
bloomington = division.teams.find_by_cms_code(428867)
edina = division.teams.find_by_cms_code(428872)
centennial = division.teams.find_by_cms_code(428864)
prior_lake = division.teams.find_by_cms_code(428869)
st_paul = division.teams.find_by_cms_code(428674)

#------ Bait Bucket Results ------#
bloomington.home_games.create!(cms_code: -1000,
  location: 'Jefferson High School - South',
  away_team: edina,
  home_team_score: 10, 
  away_team_score: 7,
  division: division,
  start_date: DateTime.parse('2016-6-11 15:00'),
  commentary: 'Bloomington Bait Bucket Battle')

minnetonka.home_games.create!(cms_code: -1001,
  location: 'Jefferson High School - Turf',
  away_team: prior_lake,
  home_team_score: 7, 
  away_team_score: 1,
  division: division,
  start_date: DateTime.parse('2016-6-11 15:00'),
  commentary: 'Bloomington Bait Bucket Battle')

bloomington.home_games.create!(cms_code: -1002,
  location: 'Jefferson High School - Turf',
  away_team: minnetonka,
  home_team_score: 1, 
  away_team_score: 15,
  division: division,
  start_date: DateTime.parse('2016-6-11 19:00'),
  commentary: 'Bloomington Bait Bucket Battle')

edina.home_games.create!(cms_code: -1003,
  location: 'Jefferson High School - South',
  away_team: prior_lake,
  home_team_score: 9, 
  away_team_score: 3,
  division: division,
  start_date: DateTime.parse('2016-6-11 19:00'),
  commentary: 'Bloomington Bait Bucket Battle')

edina.home_games.create!(cms_code: -1004,
  location: 'Jefferson High School - East',
  away_team: stillwater,
  home_team_score: 3, 
  away_team_score: 6,
  division: division,
  start_date: DateTime.parse('2016-6-12 18:30'),
  commentary: 'Bloomington Bait Bucket Battle')

#------ Shakopee Results ------#
st_paul.home_games.create!(cms_code: -1005,
  location: 'Shakopee H.S. - Field 6',
  away_team: eagan,
  home_team_score: 1, 
  away_team_score: 9,
  division: division,
  start_date: DateTime.parse('2016-6-11 18:00'),
  commentary: 'Shakopee River Valley Rumble')

#------ Prior Lake Results ------#
bloomington.home_games.create!(cms_code: -1006,
  location: 'Hansen Edina Reality',
  away_team: eagan,
  home_team_score: 1, 
  away_team_score: 15,
  division: division,
  start_date: DateTime.parse('2016-6-25 13:00'),
  commentary: 'Prior Lake Lacrosse Tournament')

eagan.home_games.create!(cms_code: -1007,
  location: 'Prior Lake Natural Health',
  away_team: woodbury,
  home_team_score: 12, 
  away_team_score: 0,
  division: division,
  start_date: DateTime.parse('2016-6-25 17:00'),
  commentary: 'Prior Lake Lacrosse Tournament')

bloomington.home_games.create!(cms_code: -1008,
  location: 'Hansen Edina Reality',
  away_team: woodbury,
  home_team_score: 5, 
  away_team_score: 3,
  division: division,
  start_date: DateTime.parse('2016-6-25 21:00'),
  commentary: 'Prior Lake Lacrosse Tournament')

centennial.home_games.create!(cms_code: -1009,
  location: 'Hansen Edina Reality',
  away_team: prior_lake,
  home_team_score: 4, 
  away_team_score: 4,
  division: division,
  start_date: DateTime.parse('2016-6-25 21:00'),
  commentary: 'Prior Lake Lacrosse Tournament')
