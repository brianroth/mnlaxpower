module ApplicationHelper
  def format(date)
    date.strftime('%F')
  end

  def opponent(team, game)
    if game.home_team == team
      game.away_team
    else
      game.home_team
    end
  end

  def goals_for(team, game)
    if game.home_team == team
      game.home_team_score
    else
      game.away_team_score
    end
  end

  def goals_against(team, game)
    if game.home_team == team
      game.away_team_score
    else
      game.home_team_score
    end
  end
end