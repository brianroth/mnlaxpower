require 'rails_helper'

describe ApplicationController do

  before { create :season, default: true }

  controller do
    def index
      raise 'unhandled exception'
    end
  end

  describe 'system error handling' do
    before do
      get :index, {}
    end

    it { is_expected.to respond_with :internal_server_error }

  end
end
