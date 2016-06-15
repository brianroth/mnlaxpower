require 'rails_helper'

describe SeasonsController do
  it { should route(:get, '/seasons/123').to(action: :show, id: '123') }

  let!(:season) { create :season, default: true }
  let!(:division) { season.divisions.create!(name: 'U15-B', cms_code: 8374, default: true) }

  describe 'GET /seasons/:id (show)' do
    describe 'when the season is found' do
      before do
        get :show, {id: season.id}
      end

      it { is_expected.to redirect_to division_url(division) }
    end

    describe 'when the season is not found' do
      before do
        get :show, {id: 'foo'}
      end

      it { is_expected.to respond_with :not_found }
    end
  end
end
