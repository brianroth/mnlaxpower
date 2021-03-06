class TeamsController < ApplicationController
  def show
    if @team = Team.find_by_id(params[:id])
      @division = @team.division
      @season = @division.season
      render :show
    else
      render_404
    end
  end
end
