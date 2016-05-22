class UpdateFeedWorker
  include Sidekiq::Worker
  def perform(season_id)
    data = { 
      org: 'youthlaxmn.org',
      season: season_id
    }

    data = get(data).first

    season = create_season(data['season'], season_id)

    divisions = data['SubDivisions']

    divisions.each do |division|
      if 'Misc Teams' == division["Name"]
        division['SubDivisions'].each do |ssd|
          d = create_division(season, ssd['Name'], ssd['ID'])
          ssd['SubDivisions'].each do |sssd|
            sssd['Teams'].each do |team|
              create_team(d, team['Name'], team['ID'])
            end
          end
        end
      else
        logger.info "Creating division #{division['Name']}"
        d = create_division(season, division['Name'], division['ID'])
        division['SubDivisions'].each do |ssd|
          ssd['Teams'].each do |team|
            create_team(d, team['Name'], team['ID'])
          end
        end
      end
    end

    set_defaults

    season.divisions.each do |division|
      UpdateDivisionWorker.perform_async division.id
    end
  end

  def create_season(name, cms_code)
    if season = Season.find_by_cms_code(cms_code)
      logger.info "Updating season #{cms_code} with name #{name}"
      season.update_attributes(name: name)
      season
    else
      logger.info "Creating season #{cms_code} with name #{name}"
      season = Season.create!(name: name, cms_code: cms_code)
    end
  end

  def create_division(season, name, cms_code)
    if division = season.divisions.find_by_cms_code(cms_code)
      logger.info "Updating division #{cms_code} with name #{name}"
      division.update_attributes(name: name)
      division
    else
      logger.info "Creating division #{cms_code} with name #{name}"
      division = season.divisions.create!(name: name, cms_code: cms_code)
    end
  end

  def create_team(division, name, cms_code)
    if team = division.teams.find_by_cms_code(cms_code)
      logger.info "Updating team #{cms_code} with name #{name}"
      team.update_attributes(name: name)
      team
    else
      logger.info "Creating team #{cms_code} with name #{name}"
      team = division.teams.create(name: name, cms_code: cms_code)
    end
  end

  def set_defaults
    if season = Season.find_by_cms_code('13414')
      season.update_attributes(default: true)

      if division = season.divisions.find_by_name('BOYS U15A')
        division.update_attributes(default: true)
      end
    end
  end

  def get(data)
    response = RestClient.get 'https://api.leagueathletics.com/api/divisions', {:params => data}
    JSON.parse(response.body)
  end
end
