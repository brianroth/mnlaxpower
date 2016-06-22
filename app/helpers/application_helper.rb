module ApplicationHelper
  def opponent(team, game)
    if game.home_team.id == team.id
      game.away_team
    else
      game.home_team
    end
  end

  def goals_for(team, game)
    if game.home_team.id == team.id
      game.home_team_score
    else
      game.away_team_score
    end
  end

  def goals_against(team, game)
    if game.home_team.id == team.id
      game.away_team_score
    else
      game.home_team_score
    end
  end

  def display_start_date(game)
    if game.start_date
      game.start_date.in_time_zone("America/Chicago").strftime("%m-%d-%y %l:%M %p")
    else
      "#{game.date} #{game.time}"
    end
  end

  def display_datetime(datetime)
    datetime.in_time_zone("America/Chicago").strftime("Last updated %A, %b %d %Y %l:%M %p")
  end
end
