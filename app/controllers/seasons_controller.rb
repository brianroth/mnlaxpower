class SeasonsController < ApplicationController
  def show
    if params[:id]
      @season = Season.find_by_id(params[:id])
    end

    if @season
      redirect_to division_url(@season.divisions.default.first || @season.divisions.first)
    else
      render_404
    end
  end
end
