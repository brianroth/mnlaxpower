class DivisionsController < ApplicationController
  def default
    if @division
      render :show
    else
      render_404
    end
  end
  
  def show
    if @division = Division.find_by_id(params[:id])
      @season = @division.season
    end

    if @division
      render :show
    else
      render_404
    end
  end
end
