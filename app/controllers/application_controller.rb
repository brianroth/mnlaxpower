class ApplicationController < ActionController::Base
  rescue_from StandardError, with: :handle_error
  protect_from_forgery with: :exception
  before_action :load_defaults

  def faq
    render :faq
  end

  def render_404
    respond_to do |format|
      format.html do
        load_defaults
        render :file => "#{Rails.root}/public/404", :status => :not_found
      end
      format.xml  { head :not_found }
      format.any  { head :not_found }
    end
  end

  private
  def handle_error(ex)
    respond_to do |format|
      format.html do 
        load_defaults
        render :file => "#{Rails.root}/public/500", :status => :internal_server_error
      end
      format.xml  { head :internal_server_error }
      format.any  { head :internal_server_error }
    end
  end

  def load_defaults
    @season = Season.default.first
    @division = @season.divisions.default.first
    @seasons = Season.all
  end
end
