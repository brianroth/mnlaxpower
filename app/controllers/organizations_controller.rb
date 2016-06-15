class OrganizationsController < ApplicationController
  def index
    @organizations = Organization.all
  end

  def show
    if @organization = Organization.find_by_id(params[:id])
      render :show
    else
      render_404
    end
  end
end
