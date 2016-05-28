require 'fileutils'

class UpdateScheduleWorker
  include Sidekiq::Worker

  def perform(season_id)
    data = { 
      org: 'youthlaxmn.org',
    }

    if season = Season.find_by_cms_code(season_id)
      logger.info "Updating schedule for #{season.name} (#{season.divisions.count} divisions)"

      season.divisions.each do |division|
        logger.info "Updating schedule for #{division.name}"

        division.teams.each do |team|
          logger.info "Updating schedule for #{team.name}"

          begin
            response = get(data.merge('TeamID': team.cms_code))
            games = response['Events']
            games.each do |game|
              create_game(division, game)
            end
          rescue NoMethodError => e
            logger.error "Unable to get results for team #{team.cms_code}"
          end
        end
      end
    end
  end

  def create_game(division, params)
    home_team = division.teams.find_by_cms_code(params['Home']['ID'])
    away_team = division.teams.find_by_cms_code(params['Away']['ID'])

    if game = Game.find_by_cms_code(params['ID'])
      gametime = DateTime.parse(params['StartDate']).in_time_zone("Central Time (US & Canada)")

      game.update_attributes(cms_code: params['ID'],
        location: params['Facility']['Name'],
        date: gametime.strftime("%m-%d-%y"),
        time: gametime.strftime("%l:%M %p"),
        home_team: home_team,
        away_team: away_team,
        away_team_score: 0,
        home_team_score: 0,
        commentary: params['commentary'])
    else
      gametime = DateTime.parse(params['StartDate']).in_time_zone("Central Time (US & Canada)")

      game = Game.create(cms_code: params['ID'],
        location: params['Facility']['Name'],
        date: gametime.strftime("%m-%d-%y"),
        time: gametime.strftime("%l:%M %p"),
        home_team: home_team,
        away_team: away_team,
        away_team_score: 0,
        home_team_score: 0,
        commentary: params['commentary'])
    end

    if game.errors.any?
      logger.error "Unable to save game with params #{params}: #{game.errors.messages}"
    else
      logger.info("Created game #{game.cms_code}: '#{away_team.name}' at '#{home_team.name}'")
    end
  end

  def get(data)
    response = RestClient.get 'https://api.leagueathletics.com/api/schedules', {:params => data}
    JSON.parse(response.body)
  end
end
