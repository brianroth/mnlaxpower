class OrganizationsController < ApplicationController
  def show
    @season = Season.default.first
    @division = @season.divisions.default.first
    @organizations = Organization.all
  end
end
