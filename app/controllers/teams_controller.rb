class TeamsController < ApplicationController
  def show
    if @team = Team.find_by_id(params[:id])
      render :show
    else
      render_404
    end
  end
end
