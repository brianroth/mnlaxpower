require 'rails_helper'

describe DivisionsController do
  let!(:season) { create :season, default: true }
  let!(:u15b) { season.divisions.create!(name: 'U15-A', cms_code: 8374, default: true) }

  it { should route(:get, '/divisions/123').to(action: :show, id: '123') }

  describe 'GET /divisions/:id (show)' do

    describe 'when the division is found' do
      before do
        get :show, params: {id: u15b.id}
      end

      it { is_expected.to respond_with :success }
      it { is_expected.to render_template :show }

      it 'responds with the division' do
        expect(assigns[:season]).to eq(season)
        expect(assigns[:division]).to eq(u15b)
      end
    end

    describe 'when the division is not found' do
      before do
        get :show, params: {id: 'foo'}
      end

      it { is_expected.to respond_with :not_found }
    end
  end
end
