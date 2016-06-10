require 'fileutils'

class UpdateDivisionWorker
  include Sidekiq::Worker

  def perform(division_id)
    data = { 
      org: 'youthlaxmn.org',
    }

    if division = Division.find_by_id(division_id)
      division.teams.each do |team|
        begin
          response = get(data.merge('TeamID': team.cms_code))
          result = response['results']
          games = result['games']
          games.each do |game|
            create_game(division, game)
          end

          wins = Game.where("(home_team_id = ? and home_team_score > away_team_score) or (away_team_id = ? and away_team_score > home_team_score)", team.id, team.id).count
          losses = Game.where("(home_team_id = ? and home_team_score < away_team_score) or (away_team_id = ? and away_team_score < home_team_score)", team.id, team.id).count
          ties = Game.where("(home_team_id = ? or away_team_id = ?) and home_team_score = away_team_score and home_team_score != 0", team.id, team.id).count

          team.update_attributes(wins: wins, 
            ties: ties,
            losses: losses,
            home_games_count: team.home_games.count, 
            away_games_count: team.away_games.count,
            updated_at: Time.now)
        rescue NoMethodError => e
          logger.error "Unable to get results for team #{team.cms_code}"
        end
      end

      division.teams.each do |team|
        wp = team.wp
        owp = team.owp
        oowp = team.oowp
        rpi = 0.25 * team.wp + 0.5 * team.owp + 0.25 * team.oowp
        logger.info "#{team.name} rpi=#{rpi.round(3)} wp=#{wp.round(3)} owp=#{owp.round(3)}"
        team.update_attributes(rpi: rpi, updated_at: Time.now)
      end
    end
  end

  def create_game(division, params)
    home_team = division.teams.find_by_cms_code(params['home']['id'])
    away_team = division.teams.find_by_cms_code(params['away']['id'])

    if game = Game.find_by_cms_code(params['id'])
      game.update_attributes(cms_code: params['id'],
        location: params['facility']['name'],
        date: params['date'],
        time: params['time'],
        home_team: home_team,
        away_team: away_team,
        home_team_score: params['home']['score'].to_i,
        away_team_score: params['away']['score'].to_i,
        commentary: params['commentary'])
    else
      game = Game.create(cms_code: params['id'],
        location: params['facility']['name'],
        date: params['date'],
        time: params['time'],
        home_team: home_team,
        away_team: away_team,
        home_team_score: params['home']['score'].to_i,
        away_team_score: params['away']['score'].to_i,
        commentary: params['commentary'])
    end

    if game.errors.any?
      logger.error "Unable to save game with params #{params}: #{game.errors.messages}"
    else
      logger.info("Created game #{game.cms_code}: '#{away_team.name}' at '#{home_team.name}'")
    end
  end

  def get(data)
    response = RestClient.get 'https://api.leagueathletics.com/api/results', {:params => data}
    JSON.parse(response.body)
  end
end
