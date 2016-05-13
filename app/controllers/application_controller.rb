class ApplicationController < ActionController::Base
  protect_from_forgery with: :exception
  before_action :load_defaults

  def faq
    render :faq
  end

  def render_404
    load_defaults
    respond_to do |format|
      format.html { render :file => "#{Rails.root}/public/404", :status => :not_found }
      format.xml  { head :not_found }
      format.any  { head :not_found }
    end
  end

  private
  def load_defaults
    @season = Season.default.first
    @division = @season.divisions.default.first
    @seasons = Season.all
  end
end
