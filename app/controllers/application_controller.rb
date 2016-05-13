class ApplicationController < ActionController::Base
  protect_from_forgery with: :exception

  def render_404
    respond_to do |format|
      format.html { render :file => "#{Rails.root}/public/404", :status => :not_found }
      format.xml  { head :not_found }
      format.any  { head :not_found }
    end
  end
end
