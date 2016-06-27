desc "This task is called by the Heroku scheduler add-on"
task :update_feed => :environment do
  puts "Updating season 13414"
  UpdateFeedWorker.perform_async 13414
  # UpdateFeedWorker.perform_async 12179
  # UpdateFeedWorker.perform_async 10141
  # UpdateFeedWorker.perform_async 8997 
  # UpdateFeedWorker.perform_async 6981 
  # UpdateFeedWorker.perform_async 5791 
  # UpdateFeedWorker.perform_async 4143 
  # UpdateFeedWorker.perform_async 3708 
  # UpdateFeedWorker.perform_async 3394 
  # UpdateFeedWorker.perform_async 2733 
  # UpdateFeedWorker.perform_async 1687 
  puts "done."
end

desc "Associate teams with organizations"
task :update_organizations => :environment do

  ['ACYLA', 'Andover', 'Anoka', 'Apple', 'Blaine', 'Blooming', 
  'Burnsville', 'Centennial', 'Chaska', 'Chisago', 'Cottage', 'Eagan', 
  'Eastview', 'Eden', 'Edina', 'Elk', 'Farmington', 'Forest', 'Hastings',
  'Hopkins', 'Hudson', 'Inver', 'Lakeville', 'Mahtomedi', 'Maple',
  'Mendota', 'Minneapolis', 'Minnetonka', 'Mound', 'Northfield',
  'Oakdale', 'Orono', 'Prior', 'Rochester', 'Rogers', 'Rosemount',
  'Roseville', 'Shakopee', 'Stillwater', 'St. Paul', 'Waconia', 
  'Wayzata', 'Westonka', 'White', 'Woodbury', 'Buffalo', 'Spring',
  'Coon', 'Delano', 'Osseo', 'New Brighton', 'Big Lake', 'Prairie Island',
  'Champlin', 'Blake', 'St. Michael', 'Breck', 'Becker', 'St. Louis Park',
  'New Bright', 'St. Cloud', 'Roger', 'RedWing', 'Saint Anthony Village'].each do |partial|

    organization = Organization.where("name LIKE :prefix", prefix: "#{partial}%").first

    if organization
      Team.where("name LIKE :prefix", prefix: "#{partial}%").each do |team|
        team.update_attributes(organization: organization)
      end
    end
  end

  # Armstrong-Cooper
  organization = Organization.find_by_name('ACYLA')
  Team.where("name LIKE :prefix", prefix: "Armstrong%").each do |team|
    team.update_attributes(organization: organization)
  end

  # Chaska-Chanhassen
  organization = Organization.find_by_name('Chaska-Chanhassen')
  Team.where("name LIKE :prefix", prefix: "Chanhassen%").each do |team|
    team.update_attributes(organization: organization)
  end
  Team.where("name LIKE :prefix", prefix: "C3%").each do |team|
    team.update_attributes(organization: organization)
  end

  # St. Michael-Albertville
  organization = Organization.find_by_name('St. Michael-Albertville')
  Team.where("name LIKE :prefix", prefix: "St. Michael%").each do |team|
    team.update_attributes(organization: organization)
  end
  Team.where("name LIKE :prefix", prefix: "St.Michael%").each do |team|
    team.update_attributes(organization: organization)
  end
  Team.where("name LIKE :prefix", prefix: "STMA%").each do |team|
    team.update_attributes(organization: organization)
  end

  # St. Paul
  organization = Organization.find_by_name('St. Paul')
  Team.where("name LIKE :prefix", prefix: "St Paul%").each do |team|
    team.update_attributes(organization: organization)
  end
  Team.where("name LIKE :prefix", prefix: "Saint Paul%").each do |team|
    team.update_attributes(organization: organization)
  end
  Team.where("name LIKE :prefix", prefix: "StPaul%").each do |team|
    team.update_attributes(organization: organization)
  end
  
  # Centennial
  organization = Organization.find_by_name('Centennial')
  Team.where("name LIKE :prefix", prefix: "Lino%").each do |team|
    team.update_attributes(organization: organization)
  end

  # Becker-Big Lake
  organization = Organization.find_by_name('Becker-Big Lake')
  Team.where("name LIKE :prefix", prefix: "Big Lake%").each do |team|
    team.update_attributes(organization: organization)
  end

  # North West Lacrosse Club
  organization = Organization.find_by_name('North West Lacrosse Club')
  Team.where("name LIKE :prefix", prefix: "NWLC%").each do |team|
    team.update_attributes(organization: organization)
  end
  Team.where("name LIKE :prefix", prefix: "Northwest%").each do |team|
    team.update_attributes(organization: organization)
  end
  Team.where("name LIKE :prefix", prefix: "North West%").each do |team|
    team.update_attributes(organization: organization)
  end

  # St. Cloud
  organization = Organization.find_by_name('St. Cloud')
  Team.where("name LIKE :prefix", prefix: "St.Cloud%").each do |team|
    team.update_attributes(organization: organization)
  end

  Organization.all.each do |organization|
    team_count = organization.teams.count
    wins = organization.teams.sum(:wins)
    games_count = organization.teams.sum(:home_games_count) + organization.teams.sum(:away_games_count)

    organization.update_attributes(wins: wins, teams_count: team_count, games_count: games_count)
  end
end
