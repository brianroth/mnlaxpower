class OrganizationsController < ApplicationController
  def index
    @season = Season.default.first
    @division = @season.divisions.default.first
    @organizations = Organization.all
  end

  def show
    if @organization = Organization.find_by_id(params[:id])
      @season = Season.default.first
      @division = @season.divisions.default.first
      render :show
    else
      render_404
    end
  end
end
