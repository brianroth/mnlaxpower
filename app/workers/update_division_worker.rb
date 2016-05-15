
class UpdateDivisionWorker
  include Sidekiq::Worker

  def perform(division_id)
    data = { 
      org: 'youthlaxmn.org',
    }

    division = Division.find_by_id(division_id)

    division.teams.each do |variable|
      data = get(data).first


    end
  end

  def get(data)
    response = RestClient.get 'https://api.leagueathletics.com/api/results', {:params => data}
    JSON.parse(response.body)
  end
end
