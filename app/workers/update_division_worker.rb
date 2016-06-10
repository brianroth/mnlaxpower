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
        wp = wp(team)
        owp = owp(team)
        oowp = oowp(team)
        rpi = 0.25 * wp + 0.5 * owp + 0.25 * oowp
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

  def wp(team)
    games_played = team.wins + team.losses + team.ties
    games_won = team.wins

    if games_played == 0
      0
    else
      (1.0 * games_won) / games_played
    end
  end

  def owp(team)
    games_played = 0
    games_won = 0

    team.games.played.each do |game|
      opponent = if game.home_team = team
        game.away_team
      else
        game.home_team
      end

      games_played += (opponent.wins + opponent.losses + opponent.ties)
      games_won += opponent.wins
    end

    if games_played == 0
      0
    else
      (1.0 * games_won) / games_played
    end
  end

  def oowp(team)
    games_played = 0
    games_won = 0

    team.games.played.each do |game|
      opponent = if game.home_team = team
        game.away_team
      else
        game.home_team
      end

      opponent.games.played.each do |opponent_game|
        opponent_opponent = if game.home_team = opponent
          game.away_team
        else
          game.home_team
        end
      
        games_played += (opponent_opponent.wins + opponent_opponent.losses + opponent_opponent.ties)
        games_won += opponent_opponent.wins
      end
    end

    if games_played == 0
      0
    else
      (1.0 * games_won) / games_played
    end
  end

  def get(data)
    response = RestClient.get 'https://api.leagueathletics.com/api/results', {:params => data}
    JSON.parse(response.body)
  end
end
