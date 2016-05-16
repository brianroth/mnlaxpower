
class UpdateDivisionWorker
  include Sidekiq::Worker

  def perform(division_id)
    data = { 
      org: 'youthlaxmn.org',
    }

    division = Division.find_by_id(division_id)

    division.teams.each do |team|

      data.merge!('TeamID': team.cms_code)
      response = get(data)
      result = response['results']
      games = result['games']
      games.each do |game|
        create_game(division, game)
      end
    end
  end

  def create_game(division, params)
    home_team = division.teams.find_by_cms_code(params['home']['id'])
    away_team = division.teams.find_by_cms_code(params['away']['id'])

    date = params['ISOStart']

    if game = Game.find_by_cms_code(params['id'])
      game.update_attributes(cms_code: params['id'],
        location: params['facility']['name'],
        home_team: home_team,
        away_team: away_team,
        home_team_score: params['home']['score'].to_i,
        away_team_score: params['away']['score'].to_i,
        date: DateTime.now,
        commentary: params['commentary'])
    else
      game = Game.create(cms_code: params['id'],
        location: params['facility']['name'],
        home_team: home_team,
        away_team: away_team,
        home_team_score: params['home']['score'].to_i,
        away_team_score: params['away']['score'].to_i,
        date: DateTime.now,
        commentary: params['commentary'])
    end

    if game.errors.any?
      logger.error "Unable to save game with params #{params}: #{game.errors.messages}"
    else
      logger.info("Created game #{game.cms_code}: '#{away_team.name}' at #{home_team.name}'")
    end
  end

  def get(data)
    response = RestClient.get 'https://api.leagueathletics.com/api/results', {:params => data}
    JSON.parse(response.body)
  end
end
