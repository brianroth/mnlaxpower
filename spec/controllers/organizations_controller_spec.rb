require 'rails_helper'

describe OrganizationsController do
  it { should route(:get, '/organizations/123').to(action: :show, id: '123') }
  it { should route(:get, '/organizations').to(action: :index) }

  let!(:season) { create :season, default: true }
  let!(:division) { season.divisions.create!(name: 'U15-B', cms_code: 8374, default: true) }
  let!(:lakeville) { create :organization, name: 'Lakeville' }
  let!(:eastview) { create :organization, name: 'Eastview' }
  let!(:rosemount) { create :organization, name: 'Rosemount' }

  describe 'GET /organizations' do
    before do
      get :index
    end

    it { is_expected.to respond_with :success }
    it { is_expected.to render_template :index }

    it 'responds with all organization in order' do
      expect(assigns[:season]).to eq(season)
      expect(assigns[:division]).to eq(division)
      expect(assigns[:organizations]).to eq([eastview,lakeville,rosemount])
    end
  end

  describe 'GET /organizations/:id (show)' do

    describe 'when the organization is found' do
      before do
        get :show, {id: lakeville.id}
      end

      it { is_expected.to respond_with :success }
      it { is_expected.to render_template :show }

      it 'responds with the organization' do
        expect(assigns[:season]).to eq(season)
        expect(assigns[:division]).to eq(division)
        expect(assigns[:organization]).to eq(lakeville)
      end
    end

    describe 'when the organization is not found' do
      before do
        get :show, {id: 'foo'}
      end

      it { is_expected.to respond_with :not_found }
    end
  end
end
