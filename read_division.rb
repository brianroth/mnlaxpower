#!/usr/bin/env ruby

require 'rest-client'
require 'json'

class ReadDivision

  def run
    data = { 
      org: 'youthlaxmn.org',
      'TeamID': 44664
    }

    data = get(data)

    results = data['results']

    games = results['games']

    games.each do |game|
      create_game(game)
    end
  end

  def create_game(params)
    date = params['ISOStart']
    location = params['facility']['name']
    home_team_cms_code = params['home']['id']
    home_team_score = params['home']['score']

    away_team_cms_code = params['away']['id']
    away_team_score = params['away']['score'] 

    puts "Creating game:\n\tdate=#{date}\n\tlocation=#{location}\n\thome[cms_code]=#{home_team_cms_code}\n\thome[score]=#{home_team_score}\n\taway[cms_code]=#{away_team_cms_code}\n\taway[score]=#{away_team_score}"
  end

  def get(data)
    response = RestClient.get 'https://api.leagueathletics.com/api/results', {:params => data}
    JSON.parse(response.body)
  end
end

if __FILE__ == $0
  ReadDivision.new.run
end