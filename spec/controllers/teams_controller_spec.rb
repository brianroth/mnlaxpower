require 'rails_helper'

describe TeamsController do
  it { should route(:get, '/teams/123').to(action: :show, id: '123') }

  let!(:season) { create :season, default: true }
  let(:division) { season.divisions.create!(name: 'U15-B', cms_code: 8374, default: true) }
  let(:team) { division.teams.create!(name: 'Eagan', cms_code: 1234) }

  describe 'GET /teams/:id (show)' do
    describe 'when the team is found' do
      before do
        get :show, {id: team.id}
      end

      it { is_expected.to respond_with :success }
      it { is_expected.to render_template :show }

      it 'responds with the team' do
        expect(assigns[:season]).to eq(season)
        expect(assigns[:division]).to eq(division)
        expect(assigns[:team]).to eq(team)
      end
    end

    describe 'when the team is not found' do
      before do
        get :show, {id: 'foo'}
      end

      it { is_expected.to respond_with :not_found }
    end
  end
end
