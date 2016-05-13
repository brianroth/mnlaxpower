class DivisionsController < ApplicationController
  def show
    if params[:id]
      @division = Division.find_by_id(params[:id])
    else
      @division = Division.first
    end

    if @division
      @season = @division.season
      render :show
    else
      render_404
    end
  end
end
