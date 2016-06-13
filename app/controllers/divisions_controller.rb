class DivisionsController < ApplicationController
  def show
    if params[:id]
      @division = Division.find_by_id(params[:id])
      @season = @division.season if @division
    else
      @season = Season.default.first
      @division = @season.divisions.default.first if @season
    end

    if @division
      render :show
    else
      render_404
    end
  end
end
