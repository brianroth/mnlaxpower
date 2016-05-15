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

    puts games
  end

{
  "id"=>611328, 
  "assocID"=>2336, 
  "date"=>"06-29-2006", 
  "time"=>"06:00 PM", 
  "ISOStart"=>"2006-06-29T23:00:00.000Z", 
  "finish"=>"07:00 PM", 
  "ISOFinish"=>"2006-06-30T00:00:00.000Z", 
  "type"=>"Game", 
  "cancelled"=>false, 
  "facility"=>{
    "id"=>65067, 
    "name"=>"Centennial MS Field 9", 
    "city"=>"Lino Lakes", "state"=>"MN"}, 
    "home"=>{
      "id"=>44657, 
      "associd"=>2336, 
      "name"=>"Lino Lakes", 
      "score"=>4, 
      "points"=>0, 
      "logo"=>""
    }, 
    "away"=>{
      "id"=>44664, 
      "associd"=>2336, 
      "name"=>"Andover Black", 
      "score"=>4, 
      "points"=>0, 
      "logo"=>""
    }, 
    "statistics"=>false, 
    "edited"=>"2013-05-28T21:25:00.000Z", 
    "editedBy"=>"MM", 
    "sent"=>nil, 
    "hasStats"=>false, 
    "commentary"=>""
  }

  def create_game(params)
    
  end
  def get(data)
    response = RestClient.get 'https://api.leagueathletics.com/api/results', {:params => data}
    JSON.parse(response.body)
  end
end

if __FILE__ == $0
  ReadDivision.new.run
end