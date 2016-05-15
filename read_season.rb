#!/usr/bin/env ruby

require 'rest-client'
require 'json'

class ReadSeason

    # 12179 "Boys Summer 2015"
    # 10141 "Boys Summer 2014"
    # 8997 "Boys Summer 2013"
    # 6981 "Boys Summer 2012"
    # 5791 "Boys Summer 2011"
    # 4143 "Boys Summer 2010"
    # 3708 "Boys Summer 2009"
    # 3394 "Boys Summer 2008"
    # 2733 "Boys Summer 2007"
    # 1687 "Boys Summer 2006"

  def run
    data = { 
      org: 'youthlaxmn.org',
      season: 4143
    }

    data = get(data).first

    season_name = data['season']

    divisions = data['SubDivisions']

    divisions.each do |division|
      if 'Misc Teams' == division["Name"]
        division['SubDivisions'].each do |ssd|
          ssd['SubDivisions'].each do |sssd|
            sssd['Teams'].each do |team|
              puts "#{season_name} #{ssd["Name"]} #{team['ID']} #{team['Name']}"
            end
          end
        end
      else
        division['SubDivisions'].each do |ssd|
          ssd['Teams'].each do |team|
            puts "#{season_name} #{division["Name"]} #{team['ID']} #{team['Name']}"
          end
        end
      end
    end
  end

  def get(data)
    response = RestClient.get 'https://api.leagueathletics.com/api/divisions', {:params => data}
    JSON.parse(response.body)
  end
end

if __FILE__ == $0
  ReadSeason.new.run
end